package com.treader.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping(path="/") // Map ONLY GET Requests
    public @ResponseBody
    ResponseEntity<?> site (Model model
            , HttpSession session) {
        return ResponseEntity.ok("wtf");
    }
}
