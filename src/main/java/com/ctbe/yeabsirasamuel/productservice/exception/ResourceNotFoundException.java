package com.ctbe.yeabsirasamuel.productservice.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Long id) {
        super("Resource " + id + " not found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
