package com.yatai.suningfiredepartment.entity;

/**
 * chc
 * 2018/5/29
 * 登陆
 */
public class LoginEntity {
    private String code;
    private String message;
    private Token data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Token getData() {
        return data;
    }

    public void setData(Token data) {
        this.data = data;
    }
}
