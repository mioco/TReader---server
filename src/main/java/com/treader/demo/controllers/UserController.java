package com.treader.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;

import com.treader.demo.model.User;
import com.treader.demo.repository.UserRepository;
import com.treader.demo.mail.EmailService;

import javax.mail.MessagingException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.security.MessageDigest;
import java.math.BigInteger;
import org.json.simple.JSONObject;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    public EmailService emailService;

    public final String getMD5 (String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes(),0,str.length());
        return new BigInteger(1,md.digest()).toString(16);
    }

    @PostMapping(path="/register")
    public ResponseEntity<?> addNewUser (@RequestBody Map<String, Object> payload
            , HttpSession session) throws NoSuchAlgorithmException {
        String captcha = payload.get("captcha").toString();
        String requireEmail = payload.get("email").toString();
        String password = payload.get("password").toString();
        String session_captcha = session.getAttribute("captcha").toString();
        String email = session.getAttribute("email").toString();
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
        User n = new User();
        n.setPassword(getMD5(password));
        n.setEmail(email);
        userRepository.save(n);

        JSONObject res = new JSONObject();
        res.put("message", "注册成功！");
        return ResponseEntity.ok(res);
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @PostMapping(path="/captcha")
    public ResponseEntity<?> captcha (HttpSession session
            , @RequestBody Map<String, Object> payload) throws MessagingException {
        String email = payload.get("email").toString();
        if (!VALID_EMAIL_ADDRESS_REGEX.matcher(email).find()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无效的邮箱！");
        }
        Random rnd = new Random();
        int captcha = 100000 + rnd.nextInt(900000);

        emailService.sendMailTemplate(email, "TReader - 注册验证码", "" + captcha);
        session.setAttribute("captcha", captcha);
        session.setAttribute("requireTime", System.currentTimeMillis());
        session.setAttribute("email", email);

        JSONObject res = new JSONObject();
        res.put("message", "发送成功！");
        return ResponseEntity.ok(res);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login (HttpSession session
            , @RequestBody Map<String, Object> payload) throws NoSuchAlgorithmException {
        String email = payload.get("email").toString();
        String password = payload.get("password").toString();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("账号不存在！");
        }
        String passwd = user.getPassword();
        if (!passwd.equals(getMD5(password))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("账号或密码错误！");
        }
        JSONObject res = new JSONObject();
        res.put("email", email);
        res.put("role", 1);
        return ResponseEntity.ok(res);
    }
    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }
}
