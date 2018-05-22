package com.treader.demo.service;


import com.treader.demo.model.User;
import com.treader.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveOne(User user) {
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
            return userRepository.findByEmail(email);
    }
}
