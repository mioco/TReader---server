package com.treader.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.ui.Model;

import com.treader.demo.configs.WebSecurityConfig;
import com.treader.demo.model.Users;
import com.treader.demo.repository.UserRepository;

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
