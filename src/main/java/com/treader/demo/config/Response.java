package com.treader.demo.config;

public class Response<T> {

    private boolean success;
    private String errMsg;
    private Integer errCode;
    private T data;

    private Response(boolean success, String errMsg, Integer errCode, T data) {
        this.success = success;
        this.errMsg = errMsg;
        this.errCode = errCode;
        this.data = data;
    }

    public static <T> Response success(T data) {
        return new Response<>(true, null, null, data);
    }

    public static Response<Void> failure(String errMsg, Integer errCode) {
        return new Response<>(false, errMsg, errCode, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
