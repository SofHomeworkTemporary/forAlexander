package com.example.mvpmongo.exception;

public class MongoException extends RuntimeException {
    public MongoException() {
        super();
    }

    public MongoException(String message) {
        super(message);
    }
}
