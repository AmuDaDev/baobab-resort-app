package com.baobab.resort.exception;

import org.springframework.http.HttpStatus;

/**
 * @author AmuDaDev
 * @created 11/12/2023
 */
public class BaobabAPIException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public BaobabAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public BaobabAPIException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
