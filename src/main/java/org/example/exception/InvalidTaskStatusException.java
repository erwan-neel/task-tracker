package org.example.exception;

public class InvalidTaskStatusException extends Exception{
    public InvalidTaskStatusException(String message) {
        super(message);
    }
}
