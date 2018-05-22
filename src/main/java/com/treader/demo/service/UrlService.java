package com.treader.demo.service;

import com.treader.demo.model.Url;
import com.treader.demo.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlService {

    private UrlRepository urlRepository;

    public Url saveOne(Url url) {
        return urlRepository.save(url);
    }

    public List<Url> findAll() {
         return urlRepository.findAll();
    }
}
