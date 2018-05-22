package com.treader.demo.service;

import com.treader.demo.model.TagUrl;
import com.treader.demo.repository.TagUrlRepository;
import org.springframework.stereotype.Service;

@Service
public class TagUrlService {

    private TagUrlRepository tagUrlRepository;

    public TagUrl saveOne(TagUrl tagUrl) {
        return tagUrlRepository.save(tagUrl);
    }

}
