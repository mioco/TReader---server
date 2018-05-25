package com.treader.demo.service;


import com.treader.demo.dto.SubscriptionDTO;
import com.treader.demo.dto.UserDTO;
import com.treader.demo.dto.UserUrlTagDTO;
import com.treader.demo.model.User;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

public interface UserService {

    UserDTO saveOne(UserDTO userDTO) throws NoSuchAlgorithmException;

    UserDTO findByEmail(String email);

    UserDTO resetPassword(String email, String password) throws NoSuchAlgorithmException;

    UserDTO login(HttpSession session, String email, String password) throws NoSuchAlgorithmException;

    void addSubscriptionUrl(User user, SubscriptionDTO subscriptionDTO);

    UserUrlTagDTO findByEmailWithUrl(String email);

}
