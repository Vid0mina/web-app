package ru.otus.ushakova.server;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}