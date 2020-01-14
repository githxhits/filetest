package com.example.demo.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(final String message) {
        super(message);
    }
    
    public ResourceNotFoundException(final String message, Throwable cause) {
        super(message, cause);
    }

}
