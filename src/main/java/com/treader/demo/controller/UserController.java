package com.treader.demo.controller;

import com.google.code.kaptcha.Constants;
import com.treader.demo.config.Response;
import com.treader.demo.dto.SubscriptionDTO;
import com.treader.demo.dto.UserDTO;
import com.treader.demo.dto.UserRegisterDTO;
import com.treader.demo.dto.UserUrlTagDTO;
import com.treader.demo.exception.CustomError;
import com.treader.demo.exception.LocalException;
import com.treader.demo.model.*;
import com.treader.demo.service.*;
import com.treader.demo.util.RedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private EmailService emailService;
    private RedisService redisService;
    private UrlService urlService;

    @Autowired
    public UserController(UserService userService, EmailService emailService, RedisService redisService, UrlService urlService) {
        this.userService = userService;
        this.emailService = emailService;
        this.redisService = redisService;
        this.urlService = urlService;
    }

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserRegisterDTO userRegisterDTO, HttpSession session) throws NoSuchAlgorithmException {

        String email = userRegisterDTO.getEmail();
        String captcha = redisService.getFromCache("captcha-" + email);
        //验证码过期
        if (StringUtils.isEmpty(captcha)) {
            throw new LocalException(CustomError.CAPTCHA_EXPRITE);
        }

        //验证码错误
        if (!captcha.equals(userRegisterDTO.getCaptcha())) {
            throw new LocalException(CustomError.CAPTCHA_WRONG);
        }

        String ksid = UUID.randomUUID().toString().replace("-", "");

        UserDTO userDTO = new UserDTO();
        userDTO.setPassword(userRegisterDTO.getPassword());
        userDTO.setEmail(email);
        userDTO.setKsid(ksid);
        userDTO = userService.saveOne(userDTO);

        return userDTO;
    }

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @PostMapping("/captcha")
    public boolean captcha(HttpSession session, @RequestParam String email) throws MessagingException {

        if (!VALID_EMAIL_ADDRESS_REGEX.matcher(email).find()) {
            throw new LocalException(CustomError.INVALID_EMAIL);
        }
        if (userService.findByEmail(email) != null) {
            throw new LocalException(CustomError.ALREADY_REGISTER);
        }
        Random rnd = new Random();
        int captcha = 100000 + rnd.nextInt(900000);

        emailService.sendMailCode(email, "TReader - 注册验证码", "" + captcha);

        //验证码30分钟ttl
        redisService.setToCacheTTL("captcha-" + email, String.valueOf(captcha), 30, TimeUnit.MINUTES);
        return true;
    }

    @PostMapping("/login")
    public UserDTO login(HttpSession session, @RequestParam String email, @RequestParam String password) throws NoSuchAlgorithmException {
        return userService.login(session, email, password);
    }

    @PostMapping("/logout")
    public boolean logout(HttpSession session) {
        session.removeAttribute("user");
        return true;
    }

    @PostMapping("/getreseturl")
    public Response getResetUrl(@RequestBody Map<String, Object> payload,
                                HttpSession session,
                                @RequestHeader("host") String hostName) throws MessagingException {
        String code = payload.get("code").toString();
        String email = payload.get("email").toString();

        if (session.getAttribute(Constants.KAPTCHA_SESSION_KEY) != code) {
            throw new LocalException(CustomError.CAPTCHA_WRONG);
        }

        String token = UUID.randomUUID().toString().replace("-", "");

        redisService.setToCache("resetToken-" + email, token);

        emailService.sendMailResetUrl(
                email, "【TReader】密码修改确认",
                String.format("%s/resetpasswd?email=%s&token=%s", hostName, email, token)
        );

        return Response.success("重置链接已发送至邮箱，请注意查收");
    }

    @PostMapping("/resetpasswd")
    public String resetpasswd(@RequestBody Map<String, Object> payload,
                              @RequestParam("token") String token,
                              @RequestParam("email") String email,
                              HttpSession session) throws NoSuchAlgorithmException {

        if (session.getAttribute("resetToken") != token) {
            throw new LocalException(CustomError.CAPTCHA_WRONG);
        }
        userService.resetPassword(email, payload.get("passwd").toString());
        return "修改成功！";
    }

    @GetMapping("/authority")
    public UserDTO authority(HttpSession session) {
        User user = (User) session.getAttribute("user");
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @PostMapping("/addSubscriptionUrl")
    public Response addSubscriptionUrl(HttpSession httpSession,
                                       @RequestBody SubscriptionDTO subscriptionDTO) {

        User user = (User) httpSession.getAttribute("user");
        userService.addSubscriptionUrl(user, subscriptionDTO);
        return Response.success("订阅成功");
    }

    @GetMapping("/getSubscriptionUrl")
    public List<Url> getSubscriptionUrl() {
        return urlService.findAll();
    }

    @GetMapping(path = "/profile")
    public UserUrlTagDTO getAllUsers(@RequestParam String email) {
        UserUrlTagDTO userUrlDTO = userService.findByEmailWithUrl(email);
        if (userUrlDTO == null) {
            throw new LocalException(CustomError.ACCOUNT_NOT_FOUND);
        }
        return userUrlDTO;
    }

    @GetMapping("/getTags")
    public List<Tag> getTags(@RequestParam String email) {
        return userService.findAllTagsByEmail(email);
    }

}
