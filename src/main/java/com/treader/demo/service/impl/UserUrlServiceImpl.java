package com.treader.demo.service.impl;

import com.treader.demo.model.UserUrl;
import com.treader.demo.repository.UserUrlRepository;
import com.treader.demo.service.UserUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserUrlServiceImpl implements UserUrlService {

    private UserUrlRepository userUrlRepository;

    @Autowired
    public UserUrlServiceImpl(UserUrlRepository userUrlRepository) {
        this.userUrlRepository = userUrlRepository;
    }

    public UserUrl saveOne(UserUrl userUrl) {
        return userUrlRepository.save(userUrl);
    }
}
