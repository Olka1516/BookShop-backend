package com.example.BookShop.response;

import org.springframework.http.HttpStatus;

public class MessageResponse<T> {
    private Integer status;
    private String message;

    public MessageResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public MessageResponse(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }

    public MessageResponse(String message) {
        this.status = HttpStatus.OK.value();
        this.message = message;
    }

    public MessageResponse(HttpStatus status) {
        this.status = status.value();
        this.message = "";
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
