package com.treader.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceEditor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.treader.demo.model.Users;
import com.treader.demo.model.Urls;
import com.treader.demo.model.User_url;
import com.treader.demo.repository.UserRepository;
import com.treader.demo.repository.UrlsRepository;
import com.treader.demo.repository.User_urlRepository;
import com.treader.demo.service.EmailService;
import javax.mail.MessagingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.math.BigInteger;
import org.json.simple.JSONObject;
import com.google.code.kaptcha.Constants;

//import com.treader.demo.configs.WebSecurityConfig;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    public EmailService emailService;
    @Autowired
    public UrlsRepository urlsRepository;

    public final String getMD5 (String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes(),0,str.length());
        return new BigInteger(1,md.digest()).toString(16);
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

        Users n = new Users();
        n.setPassword(getMD5(password));
        n.setEmail(email);
        n.setKsid(ksid);
        userRepository.save(n);
        session.invalidate();

        return ResponseEntity.ok(n.getUser());
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @PostMapping("/captcha")
    public ResponseEntity<?> captcha (HttpSession session
            , @RequestBody Map<String, Object> payload) throws MessagingException {
        String email = payload.get("email").toString();
        if (!VALID_EMAIL_ADDRESS_REGEX.matcher(email).find()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无效的邮箱！");
        }
        if (userRepository.findByEmail(email) != null) {
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
        Users user = userRepository.findByEmail(email);
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

        Users user = userRepository.findByEmail(email);
        user.setPassword(pass_md5);

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
        System.out.println(userRepository.findByEmail(email.toString()).getKsid());
        System.out.println(ksid.toString());
        System.out.println((userRepository.findByEmail(email.toString()).getKsid() != ksid.toString()));
        if (!ksid.equals(userRepository.findByEmail(email.toString()).getKsid())) {
            res.put("message", "验证失败，请重新登录");
            return ResponseEntity.ok(res);
        }

        return ResponseEntity.ok(userRepository.findByEmail(email.toString()).getUser());
    }

    @PostMapping("/addSubscriptionUrl")
    public ResponseEntity<?> addSubscriptionUrl (@RequestBody Map<String, Object> payload) {
        String url = payload.get("url").toString();
        String tempItem1 = payload.get("tempItem1").toString();
        String tempItem2 = payload.get("tempItem2").toString();
        String email = payload.get("email").toString();
        ArrayList<String> keywords = (ArrayList<String>) payload.get("keywords");

        long id = userRepository.findByEmail(email).getId();

        Urls urls = new Urls();
        urls.setTempItem(tempItem1, tempItem2);
        urls.setUrl(url);
        urls.setKeyWords(keywords.toArray(new String[keywords.size()]));

        urlsRepository.save(urls);
        return ResponseEntity.ok("订阅成功");
    }

    @GetMapping("/getSubscriptionUrl")
    public  ResponseEntity<?> addSubscriptionUrl () {
        return ResponseEntity.ok(urlsRepository.findAll());
    }

    @GetMapping(path="/profile")
    public @ResponseBody ResponseEntity<?> getAllUsers(@RequestParam("email") String email) {
        // This returns a JSON or XML with the users
        return ResponseEntity.ok(userRepository.findByEmail(email));
    }
}
