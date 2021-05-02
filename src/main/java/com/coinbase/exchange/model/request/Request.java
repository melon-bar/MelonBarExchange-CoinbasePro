package com.coinbase.exchange.model.request;

/**
 * Interface for outbound requests.
 */
public interface Request {

    /**
     * Validation method to be implemented based on each API request's requirements. A result of true implies that
     * the request is ready for dispatch.
     *
     * @return True if the request is valid, false otherwise
     */
    boolean isValidRequest();
}
