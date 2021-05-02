package com.coinbase.exchange.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(final String message) {
        super(message);
    }

    public InvalidRequestException(final Throwable throwable) {
        super(throwable);
    }

    public InvalidRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
