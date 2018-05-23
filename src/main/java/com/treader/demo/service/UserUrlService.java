package com.treader.demo.service;

import com.treader.demo.model.UserUrl;
import com.treader.demo.repository.UserUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserUrlService {

    private UserUrlRepository userUrlRepository;

    @Autowired
    public UserUrlService(UserUrlRepository userUrlRepository) {
        this.userUrlRepository = userUrlRepository;
    }

    public UserUrl saveOne(UserUrl userUrl) {
        return userUrlRepository.save(userUrl);
    }
}
