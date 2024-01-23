package com.proj.user.exception;

public class CsrfException extends RuntimeException{

    public CsrfException(String message) {
        super(message);
    }
}
