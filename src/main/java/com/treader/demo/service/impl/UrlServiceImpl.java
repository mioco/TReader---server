package com.treader.demo.service.impl;

import com.treader.demo.model.Url;
import com.treader.demo.repository.UrlRepository;
import com.treader.demo.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlServiceImpl implements UrlService {

    private UrlRepository urlRepository;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }


    @Override
    public List<Url> findAll() {
        return urlRepository.findAll();
    }
}
