package com.melonbar.exchange.coinbase.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(final String message) {
        super(message);
    }

    public BadRequestException(final Throwable throwable) {
        super(throwable);
    }

    public BadRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
