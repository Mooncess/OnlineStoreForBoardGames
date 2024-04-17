package ru.mooncess.serverjwt.exception;

public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }

}