package com.gdas.shopadminapi.dtos;

import java.util.Collections;
import java.util.List;

public class CustomErrorResponse {

    private String message;
    private List<String> details = Collections.emptyList();

    public CustomErrorResponse(String message) {
        this.message = message;
    }

    public CustomErrorResponse(String message, List<String> details) {
        this.message = message;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getDetails() {
        return details;
    }
}
