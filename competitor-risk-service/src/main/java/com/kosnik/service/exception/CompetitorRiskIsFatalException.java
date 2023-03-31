package com.kosnik.service.exception;

public class CompetitorRiskIsFatalException extends RuntimeException{
    public CompetitorRiskIsFatalException() {
    }

    public CompetitorRiskIsFatalException(String message) {
        super(message);
    }

    public CompetitorRiskIsFatalException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompetitorRiskIsFatalException(Throwable cause) {
        super(cause);
    }
}
