package com.gdas.shopadminapi.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

public class BusinessException extends HttpStatusCodeException {

    private String messageCode;
    private Object[] args;

    public BusinessException(String formattedText, HttpStatusCode statusCode) {
        super(statusCode, formattedText);
    }

    public BusinessException(HttpStatusCode statusCode, String messageCode) {
        super(statusCode);
        this.messageCode = messageCode;
        this.args = new Object[]{};
    }

    public BusinessException(HttpStatusCode statusCode, String messageCode, Object... args) {
        super(statusCode);
        this.messageCode = messageCode;
        this.args = args;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public Object[] getArgs() {
        return args;
    }

}
