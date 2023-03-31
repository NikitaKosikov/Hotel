package com.kosnik.service.exception;

public class OrderForDateAlreadyTakenException extends Exception{
    public OrderForDateAlreadyTakenException() {
    }

    public OrderForDateAlreadyTakenException(String message) {
        super(message);
    }

    public OrderForDateAlreadyTakenException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderForDateAlreadyTakenException(Throwable cause) {
        super(cause);
    }
}
