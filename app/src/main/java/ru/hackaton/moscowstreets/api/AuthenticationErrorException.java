package ru.hackaton.moscowstreets.api;

public class AuthenticationErrorException extends Exception {
    public AuthenticationErrorException(Throwable throwable) {
        super(throwable);
    }
}
