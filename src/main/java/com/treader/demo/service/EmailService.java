package com.treader.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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

    public void sendMailCode(String to, String subject, String message) throws MessagingException {
        StringBuilder sb = new StringBuilder();
        sb.append("<p>亲爱的用户：</p>");
        sb.append("<p>您好！感谢您使用TReader，您正在进行邮箱验证，本次请求的验证码为：");
        sb.append("<span style=\"font-size: 1.5em; color: #3af\">");
        sb.append(message);
        sb.append("</span><p>");
        sendSimpleMessage(to, subject, sb.toString());
    }

    public void sendMailResetUrl(String to, String subject, String message) throws MessagingException {
        StringBuilder sb = new StringBuilder();

        sb.append("<strong>【TReader】修改密码确认</strong>");
        sb.append("<p>请点击以下链接，以确认是您本人申请修改您的密码：</p>");
        sb.append(message);
        sb.append("<p>如果以上链接不能点击，你可以复制网址URL，然后粘贴到浏览器地址栏打开，完成确认。</p>");
        sb.append("<p>（这是一封自动发送的邮件，请不要直接回复）</p>");

        sendSimpleMessage(to, subject, sb.toString());
    }
}
