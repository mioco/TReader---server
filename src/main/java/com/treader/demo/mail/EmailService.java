package com.treader.demo.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * Created by abburi on 6/10/17.
 */

@Service
public class EmailService {

    @Autowired
    @Qualifier("emailSender")
    private JavaMailSender javaMailSender;

    @Value("${email.from.address}")
    private String fromAddress;

    public void sendSimpleMessage(String to, String subject, String text) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);

        mimeMessage.setContent(text, "text/html;charset=utf-8");
        helper.setFrom(fromAddress);
        helper.setTo(to);
        helper.setSubject(subject);

        javaMailSender.send(mimeMessage);
    }

    public void sendMailTemplate(String to, String subject, String message) throws MessagingException {
        StringBuilder sb = new StringBuilder();
        sb.append("<p>亲爱的用户：</p>");
        sb.append("<p>您好！感谢您使用TReader，您正在进行邮箱验证，本次请求的验证码为：");
        sb.append("<span style=\"font-size: 1.5em; color: #3af\">");
        sb.append(message);
        sb.append("</span><p>");
        sendSimpleMessage(to, subject, sb.toString());
    }
}
