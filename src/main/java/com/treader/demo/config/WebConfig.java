package com.treader.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }


    @Bean
    public MappingJackson2HttpMessageConverter customMappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter() {
            private final Logger LOGGER = LoggerFactory.getLogger(getClass());

            @Override
            protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
                if (!(object instanceof Response)) {
                    // 当返回对象并非是 HttpResponse 时, 包装成 HttpResponse
                    object = Response.success(object);
                }
                super.writeInternal(object, type, outputMessage);
            }
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        // 排除配置
        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/");
        addInterceptor.excludePathPatterns("/user/login","/user/register","/user/captcha");
        // 拦截配置
        addInterceptor.addPathPatterns("/**");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(customMappingJackson2HttpMessageConverter());
    }
}


class SecurityInterceptor extends HandlerInterceptorAdapter {
    private final static String SESSION_KEY = "user";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();
        if (session.getAttribute(SESSION_KEY) != null)
            return true;

        //返回401
        response.setStatus(401);
        return false;
    }
}