package com.challenge.dux.exception;

public class DuplicateTeamException extends RuntimeException {
    public DuplicateTeamException(String message) {
        super(message);
    }
}