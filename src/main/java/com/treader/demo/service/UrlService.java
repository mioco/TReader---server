package com.treader.demo.service;

import com.treader.demo.model.Url;

import java.util.List;

public interface UrlService {
    List<Url> findAll();

    void deleteById(Integer id);
}
