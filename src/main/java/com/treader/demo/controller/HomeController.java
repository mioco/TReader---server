package com.treader.demo.controller;

import com.alibaba.fastjson.JSON;
import com.treader.demo.config.Response;
import com.treader.demo.model.WebPage;
import com.treader.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    private SimpMessagingTemplate template;
    private SimpUserRegistry userRegistry;

    private UserService userService;

    @Autowired
    public HomeController(SimpMessagingTemplate template, SimpUserRegistry userRegistry, UserService userService) {
        this.template = template;
        this.userRegistry = userRegistry;
        this.userService = userService;
    }

    @GetMapping("/")
    public Response site(HttpSession session) {
        return Response.success("wtf");
    }


    @Scheduled(fixedRate = 1000*10)
    public void sendBack() {
        for (SimpUser user : userRegistry.getUsers()) {
            WebPage webPage = userService.findOneWebpage(user.getName());
            if (webPage == null) {
                continue;
            }
            this.template.convertAndSendToUser(user.getName(),
                    "/topic/greetings",
                    JSON.toJSONString(webPage));
        }
    }
}
