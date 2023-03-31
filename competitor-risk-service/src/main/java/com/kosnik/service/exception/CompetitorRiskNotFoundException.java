package com.kosnik.service.exception;

public class CompetitorRiskNotFoundException extends RuntimeException{
    public CompetitorRiskNotFoundException() {
        super();
    }

    public CompetitorRiskNotFoundException(String message) {
        super(message);
    }

    public CompetitorRiskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompetitorRiskNotFoundException(Throwable cause) {
        super(cause);
    }
}
