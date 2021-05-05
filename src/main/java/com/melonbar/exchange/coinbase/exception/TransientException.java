package com.melonbar.exchange.coinbase.exception;

public class TransientException extends RuntimeException {

    public TransientException(final String message) {
        super(message);
    }

    public TransientException(final Throwable throwable) {
        super(throwable);
    }

    public TransientException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
