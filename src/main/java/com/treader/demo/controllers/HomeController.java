package com.treader.demo.controllers;

import com.treader.demo.repository.PageRepository;
import com.treader.demo.model.CrawlerPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @Autowired
    private PageRepository pageRepository;

    @GetMapping(path="/") // Map ONLY GET Requests
    public @ResponseBody Iterable<CrawlerPage> site () {
        return pageRepository.findAll();
    }
}
