package com.treader.demo.service;


import com.treader.demo.dto.SubscriptionDTO;
import com.treader.demo.dto.UrlTagDTO;
import com.treader.demo.dto.UserDTO;
import com.treader.demo.dto.UserUrlTagDTO;
import com.treader.demo.model.Tag;
import com.treader.demo.model.User;
import com.treader.demo.model.WebPage;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {

    UserDTO saveOne(UserDTO userDTO) throws NoSuchAlgorithmException;

    UserDTO findByEmail(String email);

    UserDTO resetPassword(String email, String password) throws NoSuchAlgorithmException;

    UserDTO login(HttpSession session, String email, String password) throws NoSuchAlgorithmException;

    UrlTagDTO addSubscriptionUrl(User user, SubscriptionDTO subscriptionDTO);

    UserUrlTagDTO findByEmailWithUrl(String email);

    List<Tag> findAllTagsByEmail(String email);

    WebPage findOneWebpage(String email);

}
