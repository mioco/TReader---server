package com.treader.demo.service.impl;

import com.treader.demo.dto.SubscriptionDTO;
import com.treader.demo.dto.UserDTO;
import com.treader.demo.dto.UserUrlDTO;
import com.treader.demo.exception.CustomError;
import com.treader.demo.exception.LocalException;
import com.treader.demo.model.*;
import com.treader.demo.repository.*;
import com.treader.demo.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.treader.demo.util.MD5Util.getMD5;

@Service
public class UserServiceImpl implements UserService {


    private UserRepository userRepository;
    private UrlRepository urlRepository;
    private TagRepository tagRepository;
    private TagUrlRepository tagUrlRepository;
    private UserUrlRepository userUrlRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UrlRepository urlRepository, TagRepository tagRepository, TagUrlRepository tagUrlRepository, UserUrlRepository userUrlRepository) {
        this.userRepository = userRepository;
        this.urlRepository = urlRepository;
        this.tagRepository = tagRepository;
        this.tagUrlRepository = tagUrlRepository;
        this.userUrlRepository = userUrlRepository;
    }


    @Override
    public UserDTO saveOne(UserDTO userDTO) throws NoSuchAlgorithmException {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setPassword(getMD5(userDTO.getPassword()));
        user = userRepository.save(user);
        userDTO.setId(user.getId());
        return userDTO;
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @Override
    public UserDTO resetPassword(String email, String password) throws NoSuchAlgorithmException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new LocalException(CustomError.ACCOUNT_NOT_FOUND);
        }
        user.setPassword(getMD5(password));
        userRepository.save(user);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;

    }

    @Override
    public UserDTO login(HttpSession session, String email, String password) throws NoSuchAlgorithmException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new LocalException(CustomError.ACCOUNT_NOT_FOUND);
        }
        String passwd = user.getPassword();
        if (!passwd.equals(getMD5(password))) {
            throw new LocalException(CustomError.PASSWORD_WRONG);
        }
        //设置登录状态，看see SecurityInterceptor
        session.setAttribute("user", user);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @Override
    public void addSubscriptionUrl(User user, SubscriptionDTO subscriptionDTO) {
        Url url = urlRepository.findByUrl(subscriptionDTO.getUrl());
        //防止重复添加
        if (url == null) {
            url = new Url();
            url.setTempItem(subscriptionDTO.getTempItem1(), subscriptionDTO.getTempItem2());
            url.setUrl(subscriptionDTO.getUrl());
            url = urlRepository.save(url);
        }
        Integer urlId = url.getId();

        subscriptionDTO.getKeywords().forEach(keyword -> {

            Tag tag = tagRepository.findByTag(keyword);
            if (tag == null) {
                tag = new Tag();
                tag.setTag(keyword);
                tag = tagRepository.save(tag);
            }

            TagUrl tagUrl = tagUrlRepository.findByTagIdAndUrlId(urlId, tag.getId());
            if (tagUrl == null) {
                tagUrl = new TagUrl();
                tagUrl.setTagId(tag.getId());
                tagUrl.setUrlId(urlId);
                tagUrlRepository.save(tagUrl);
            }



            UserUrl userUrl = userUrlRepository.findByUserIdAndUrlId(user.getId(), urlId);
            if (userUrl == null) {
                userUrl = new UserUrl();
                userUrl.setUrlId(urlId);
                userUrl.setUserId(user.getId());
                userUrlRepository.save(userUrl);
            }
        });
    }

    @Override
    public UserUrlDTO findByEmailWithUrl(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return null;
        }
        UserUrlDTO userUrlDTO = new UserUrlDTO();
        BeanUtils.copyProperties(user, userUrlDTO);
        List<UserUrl> userUrlList = userUrlRepository.findByUserId(user.getId());
        List<Url> urlList = userUrlList.stream()
                .map(userUrl -> {
                    Optional<Url> urlOptional = urlRepository.findById(userUrl.getUrlId());
                    return urlOptional.orElse(null);
                }).collect(Collectors.toList());
        userUrlDTO.setUrlList(urlList);
        return userUrlDTO;
    }

}
