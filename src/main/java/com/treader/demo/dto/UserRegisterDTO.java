package com.treader.demo.dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String captcha;
    private String email;
    private String password;
}
