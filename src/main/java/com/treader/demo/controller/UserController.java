package com.treader.demo.controller;

import com.google.code.kaptcha.Constants;
import com.treader.demo.dto.UserDTO;
import com.treader.demo.dto.UserRegisterDTO;
import com.treader.demo.exception.CustomError;
import com.treader.demo.exception.LocalException;
import com.treader.demo.model.*;
import com.treader.demo.service.*;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;

import static com.treader.demo.util.MD5Util.getMD5;


@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private EmailService emailService;
    private UrlService urlService;
    private TagService tagService;
    private TagUrlService tagUrlService;
    private UserUrlService userUrlService;

    @Autowired
    public UserController(UserService userService, EmailService emailService, UrlService urlService, TagService tagService, TagUrlService tagUrlService, UserUrlService userUrlService) {
        this.userService = userService;
        this.emailService = emailService;
        this.urlService = urlService;
        this.tagService = tagService;
        this.tagUrlService = tagUrlService;
        this.userUrlService = userUrlService;
    }

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserRegisterDTO userRegisterDTO, HttpSession session) throws NoSuchAlgorithmException {
        String session_captcha = session.getAttribute("captcha").toString();
        String email = session.getAttribute("email").toString();
        String ksid = UUID.randomUUID().toString().replace("-", "");


        if (!email.equals(userRegisterDTO.getEmail())) {
            throw new LocalException(CustomError.WRONG_EMAIL);
        }

        long requireTime = Long.parseLong(session.getAttribute("requireTime").toString());
        long currentTime = System.currentTimeMillis();

        if ((currentTime - requireTime) / 1000 / 60 > 30) {
            throw new LocalException(CustomError.CAPTCHA_EXPRITE);
        }
        if (!session_captcha.equals(userRegisterDTO.getCaptcha())) {
            throw new LocalException(CustomError.CAPTCHA_WRONG);
        }
        User user = new User();
        user.setPassword(getMD5(userRegisterDTO.getPassword()));
        user.setEmail(email);
        user.setKsid(ksid);
        user = userService.saveOne(user);

        session.invalidate();

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
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
        session.setAttribute("captcha", captcha);
        session.setAttribute("requireTime", System.currentTimeMillis());
        session.setAttribute("email", email);

        return true;
    }

    @PostMapping("/login")
    public UserDTO login(HttpSession session, @RequestParam String email, @RequestParam String password) throws NoSuchAlgorithmException {

        User user = userService.findByEmail(email);
        if (user == null) {
            throw new LocalException(CustomError.ACCOUNT_NOT_FOUND);
        }

        String passwd = user.getPassword();
        if (!passwd.equals(getMD5(password))) {

            throw new LocalException(CustomError.PASSWORD_WRONG);
        }
        session.setAttribute("email", user.getEmail());
        session.setAttribute("ksid", user.getKsid());
        session.setAttribute("loginTime", System.currentTimeMillis());

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @PostMapping("/logout")
    public boolean logout(HttpSession session) {
        session.invalidate();
        return true;
    }

    @PostMapping("/getreseturl")
    public String getResetUrl(@RequestBody Map<String, Object> payload,
                              HttpSession session,
                              @RequestHeader("host") String hostName) throws MessagingException {
        String code = payload.get("code").toString();
        String email = payload.get("email").toString();

        if (session.getAttribute(Constants.KAPTCHA_SESSION_KEY) != code) {
            throw new LocalException(CustomError.CAPTCHA_WRONG);
        }

        String token = UUID.randomUUID().toString().replace("-", "");
        session.setAttribute("resetToken", token);
        emailService.sendMailResetUrl(
                email, "【TReader】密码修改确认",
                String.format("%s/resetpasswd?email=%s&token=%s", hostName, email, token)
        );

        return "重置链接已发送至邮箱，请注意查收";
    }

    @PostMapping("/resetpasswd")
    public String resetpasswd(@RequestBody Map<String, Object> payload,
                              @RequestParam("token") String token,
                              @RequestParam("email") String email,
                              HttpSession session) throws NoSuchAlgorithmException {

        if (session.getAttribute("resetToken") != token) {
            throw new LocalException(CustomError.CAPTCHA_WRONG);
        }
        String pass_md5 = getMD5(payload.get("passwd").toString());

        User user = userService.findByEmail(email);
        user.setPassword(pass_md5);
        userService.saveOne(user);

        return "修改成功！";
    }

    @PostMapping("/authority")
    public ResponseEntity<?> authority(HttpSession session) {
        Object email = session.getAttribute("email");
        Object ksid = session.getAttribute("ksid");

        JSONObject res = new JSONObject();
        if (email == null || ksid == null) {
            res.put("message", "未登录！");
            return ResponseEntity.ok(res);
        }
        System.out.println(userService.findByEmail(email.toString()).getKsid());
        System.out.println(ksid.toString());
        System.out.println((userService.findByEmail(email.toString()).getKsid() != ksid.toString()));
        if (!ksid.equals(userService.findByEmail(email.toString()).getKsid())) {
            res.put("message", "验证失败，请重新登录");
            return ResponseEntity.ok(res);
        }

        return ResponseEntity.ok(userService.findByEmail(email.toString()).getUser());
    }

    @PostMapping("/addSubscriptionUrl")
    public ResponseEntity<?> addSubscriptionUrl(@RequestBody Map<String, Object> payload) {
        String url = payload.get("url").toString();
        String tempItem1 = payload.get("tempItem1").toString();
        String tempItem2 = payload.get("tempItem2").toString();
        String email = payload.get("email").toString();
        ArrayList<String> keywords = (ArrayList<String>) payload.get("keywords");

        User user = userService.findByEmail(email);

        Url urls = new Url();
        urls.setTempItem(tempItem1, tempItem2);
        urls.setUrl(url);
        urls = urlService.saveOne(urls);
        Integer urlId = urls.getId();

        keywords.forEach(keyword -> {
            Tag tag = new Tag();
            tag.setTag(keyword);
            tag = tagService.saveOne(tag);

            TagUrl tagUrl = new TagUrl();
            tagUrl.setTagId(tag.getId());
            tagUrl.setUrlId(urlId);
            tagUrlService.saveOne(tagUrl);

            UserUrl userUrl = new UserUrl();
            userUrl.setUrlId(urlId);
            userUrl.setUserId(user.getId());
            userUrlService.saveOne(userUrl);

        });
        return ResponseEntity.ok("订阅成功");
    }

    @GetMapping("/getSubscriptionUrl")
    public List<Url> addSubscriptionUrl() {
        return urlService.findAll();
    }

    @GetMapping(path = "/profile")
    public UserDTO getAllUsers(@RequestParam("email") String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }


}
