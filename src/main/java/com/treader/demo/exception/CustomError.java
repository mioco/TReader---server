package com.treader.demo.exception;


public enum  CustomError {

    WRONG_EMAIL(100, "错误的邮箱!"),
    CAPTCHA_EXPRITE(101, "验证码过期，请刷新！"),
    CAPTCHA_WRONG(102, "验证码错误！"),
    INVALID_EMAIL(103, "无效的邮箱!"),
    ALREADY_REGISTER(104, "该邮箱已注册，请直接登录！"),
    ACCOUNT_NOT_FOUND(105, "账号不存在！"),
    PASSWORD_WRONG(106,"账号或密码错误！"),

    USER_NOT_FOUND(200, "用户未找到"),
    ;

    private int code;

    private String errMsg;

    CustomError(int code, String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }


    public int getCode() {
        return this.code;
    }

    public String getErrMsg() {
        return errMsg;
    }

}
