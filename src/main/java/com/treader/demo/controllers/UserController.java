package com.treader.demo.controllers;

import com.google.code.kaptcha.Constants;
import com.treader.demo.model.*;
import com.treader.demo.service.*;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
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
    public ResponseEntity<?> register (@RequestBody Map<String, Object> payload
            , HttpSession session) throws NoSuchAlgorithmException {
        String captcha = payload.get("captcha").toString();
        String requireEmail = payload.get("email").toString();
        String password = payload.get("password").toString();
        String session_captcha = session.getAttribute("captcha").toString();
        String email = session.getAttribute("email").toString();
        String ksid = UUID.randomUUID().toString().replace("-", "");

        if (!email.equals(requireEmail)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("错误的邮箱！");
        }
        long requireTime = Long.parseLong(session.getAttribute("requireTime").toString());
        long currentTime = System.currentTimeMillis();
        if ((currentTime - requireTime) / 1000 / 60 > 30) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("验证码过期，请刷新！");
        }
        if (!session_captcha.equals(captcha)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("验证码错误！");
        }

        User user = new User();
        user.setPassword(getMD5(password));
        user.setEmail(email);
        user.setKsid(ksid);
        userService.saveOne(user);
        session.invalidate();
        return ResponseEntity.ok(user.getUser());
    }

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @PostMapping("/captcha")
    public ResponseEntity<?> captcha (HttpSession session
            , @RequestBody Map<String, Object> payload) throws MessagingException {
        String email = payload.get("email").toString();
        if (!VALID_EMAIL_ADDRESS_REGEX.matcher(email).find()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无效的邮箱！");
        }
        if (userService.findByEmail(email) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("该邮箱已注册，请直接登录！");
        }
        Random rnd = new Random();
        int captcha = 100000 + rnd.nextInt(900000);

        emailService.sendMailCode(email, "TReader - 注册验证码", "" + captcha);
        session.setAttribute("captcha", captcha);
        session.setAttribute("requireTime", System.currentTimeMillis());
        session.setAttribute("email", email);

        JSONObject res = new JSONObject();
        res.put("message", "发送成功！");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (HttpSession session
            , @RequestBody Map<String, Object> payload) throws NoSuchAlgorithmException {
        String email = payload.get("email").toString();
        String password = payload.get("password").toString();
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("账号不存在！");
        }
        String passwd = user.getPassword();
        if (!passwd.equals(getMD5(password))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("账号或密码错误！");
        }
        session.setAttribute("email", user.getEmail());
        session.setAttribute("ksid", user.getKsid());
        session.setAttribute("loginTime", System.currentTimeMillis());
        return ResponseEntity.ok(user.getUser());
    }

    @PostMapping("/logout")
    public String logout (HttpSession session) {
        session.invalidate();
        return "log out";
    }

    @PostMapping("/getreseturl")
    public ResponseEntity<?> getResetUrl (@RequestBody Map<String, Object> payload
            , HttpSession session
            , @RequestHeader("host") String hostName) throws MessagingException {
        String code = payload.get("code").toString();
        String email = payload.get("email").toString();

        if (session.getAttribute(Constants.KAPTCHA_SESSION_KEY) != code) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("验证码不正确！");
        }

        String token = UUID.randomUUID().toString().replace("-", "");
        session.setAttribute("resetToken", token);
        emailService.sendMailResetUrl(
            email, "【TReader】密码修改确认",
            String.format("{}/resetpasswd?email={}&token={}", hostName, email, token)
        );

        return ResponseEntity.ok("重置链接已发送至邮箱，请注意查收");
    }

    @PostMapping("/resetpasswd")
    public ResponseEntity<String> resetpasswd (@RequestBody Map<String, Object> payload
            , @RequestParam("token") String token
            , @RequestParam("email") String email
            , HttpSession session) throws NoSuchAlgorithmException {

        if (session.getAttribute("resetToken") != token) {
            return ResponseEntity.badRequest().body("验证码不正确！");
        }
        String pass_md5 = getMD5(payload.get("passwd").toString());

        User user = userService.findByEmail(email);
        user.setPassword(pass_md5);
        userService.saveOne(user);

        return ResponseEntity.ok("修改成功！");
    }

    @PostMapping("/authority")
    public ResponseEntity<?> authority (HttpSession session) {
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
    @Transactional
    public ResponseEntity<?> addSubscriptionUrl (@RequestBody Map<String, Object> payload) {
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
    public  ResponseEntity<?> addSubscriptionUrl () {
        return ResponseEntity.ok(urlService.findAll());
    }

    @GetMapping(path="/profile")
    public @ResponseBody ResponseEntity<?> getAllUsers(@RequestParam("email") String email) {
        // This returns a JSON or XML with the users
        return ResponseEntity.ok(userService.findByEmail(email));
    }


}
