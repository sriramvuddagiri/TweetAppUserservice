package com.tweetapp.exception;

@SuppressWarnings("serial")
public class UserExistsException extends RuntimeException {

    public UserExistsException(String message) {
        super(message);
    }
}
