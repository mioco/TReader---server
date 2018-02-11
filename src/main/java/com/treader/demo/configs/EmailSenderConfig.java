package com.treader.demo.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Created by abburi on 6/10/17.
 */

@Configuration
public class EmailSenderConfig {


    @Value("${email.sender.host}")
    private String host;

    @Value("${email.sender.port}")
    private String port;

    @Value("${email.sender.username}")
    private String username;

    @Value("${email.sender.password}")
    private String password;

    @Value("${email.smtp.auth}")
    private String auth;

    @Value("${email.smtp.starttls.enable}")
    private String ttlsEnable;

    @Value("${email.transport.protocol}")
    private String protocol;


    @Bean(name = "emailSender")
    public JavaMailSender getJavaMailSender(){

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        System.out.print(host);
        System.out.print(port);
        System.out.print(username);
        System.out.print(password);
        mailSender.setHost(host);
        mailSender.setPort(Integer.valueOf(port));

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        mailSender.setJavaMailProperties(getMailSenderProperties());

        return mailSender;
    }

    private Properties getMailSenderProperties(){
        Properties props = new Properties();
        props.put("mail.smtp.auth", auth);
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.starttls.enable", ttlsEnable);

        return props;
    }
}
