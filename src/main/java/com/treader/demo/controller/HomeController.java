package com.treader.demo.controller;

import com.treader.demo.config.Response;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@RestController
public class HomeController {

    @GetMapping("/")
    public Response site(HttpSession session) {
        return Response.success("wtf");
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public List<String> greeting() throws Exception {
        Thread.sleep(1000); // simulated delay
        return Arrays.asList("hello", "world", "wtf");
    }
}
