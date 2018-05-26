package com.treader.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.treader.demo.config.Response;
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
import java.util.Collections;

@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    private SimpMessagingTemplate template;
    private SimpUserRegistry userRegistry;

    @Autowired
    public HomeController(SimpMessagingTemplate template, SimpUserRegistry userRegistry) {
        this.template = template;
        this.userRegistry = userRegistry;
    }

    @GetMapping("/")
    public Response site(HttpSession session) {
        return Response.success("wtf");
    }


    @Scheduled(fixedRate = 20000)
    public void sendBack() {
        for (SimpUser user : userRegistry.getUsers()) {
            this.template.convertAndSendToUser(user.getName(),
                    "/topic/greetings",
                    JSON.toJSONString(Collections.singletonMap("email", user.getName())));
        }
    }
}
