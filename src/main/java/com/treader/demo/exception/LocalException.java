package com.treader.demo.exception;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class LocalException extends RuntimeException {
    private CustomError error;
    private List<String> parameters = new ArrayList<>();

    public LocalException(CustomError error) {
        this.error = error;
    }

    public LocalException(CustomError error, String message) {
        super(message);
        this.error = error;
    }

    public LocalException(CustomError error, String message, Throwable cause) {
        super(message, cause);
        this.error = error;
    }

    public LocalException(CustomError error, Throwable cause) {
        super(cause);
        this.error = error;
    }

    public LocalException addParameter(String parameter) {
        if (StringUtils.isNotBlank(parameter)) {
            if (parameter.length() > 1000) {
                parameter = parameter.substring(0, 1000);
            }
            parameters.add(parameter);
        }
        return this;
    }

    public <T> LocalException addParameter(T parameter) {
        String param = JSON.toJSONString(parameter);
        if (StringUtils.isNotBlank(param)) {
            if (param.length() > 1000) {
                param = param.substring(0, 1000);
            }
            parameters.add(param);
        }
        return this;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public CustomError getError() {
        return error;
    }

    @Override
    public String getMessage() {
        String errMsg = error.getErrMsg();
        if (CollectionUtils.isNotEmpty(parameters)) {
            errMsg = MessageFormat.format(errMsg, parameters.toArray());
        }
        if (StringUtils.isNotBlank(super.getMessage())) {
            errMsg += ": " + super.getMessage();
        }
        return errMsg;
    }
}
