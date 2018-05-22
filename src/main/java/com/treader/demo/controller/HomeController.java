package com.treader.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class HomeController {

    @GetMapping("/")
    public boolean site(HttpSession session) {
        return true;
    }
}
