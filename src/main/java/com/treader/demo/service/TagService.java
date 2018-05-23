package com.treader.demo.service;

import com.treader.demo.model.Tag;
import com.treader.demo.repository.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private TagRepository tagRepository;

    public Tag saveOne(Tag tag) {
        return tagRepository.save(tag);
    }
}
