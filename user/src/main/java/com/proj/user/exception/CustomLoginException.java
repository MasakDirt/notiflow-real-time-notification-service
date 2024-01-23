package com.proj.user.exception;

public class CustomLoginException extends RuntimeException{

    public CustomLoginException(String message) {
        super(message);
    }
}
