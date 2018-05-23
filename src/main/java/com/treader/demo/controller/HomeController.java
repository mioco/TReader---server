package com.treader.demo.controller;

import com.treader.demo.config.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class HomeController {

    @GetMapping("/")
    public Response site(HttpSession session) {
        return Response.success("wtf");
    }
}
