package com.treader.demo.controller;

import com.treader.demo.config.Response;
import com.treader.demo.exception.LocalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController  {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response handleException(Exception ex) {
        if (ex instanceof LocalException) {
            // 自定义异常
            LocalException localException = (LocalException) ex;
            return Response.failure(localException.getMessage(), localException.getError().getCode());
        } else {
            return Response.failure(ex.getMessage(), null);
        }

    }
}
