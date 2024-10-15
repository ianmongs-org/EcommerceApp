package com.java.EcomerceApp.exception;

public class CategoryInUseException extends RuntimeException {
    public CategoryInUseException(Long id) {
        super("Category is in use: id: " + id);
    }
}
