package com.coinbase.exchange.model.request;

import com.coinbase.exchange.aspect.RequestEnrichmentAspect;
import com.coinbase.exchange.http.Http;
import com.coinbase.exchange.util.request.Pagination;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Base request definition for outbound HTTP requests to Coinbase Pro. Core request members are to be populated
 * during execution pre-processing in {@link RequestEnrichmentAspect}.
 */
public abstract class BaseRequest implements Request {

    @Getter @Setter private String requestPath;
    @Getter @Setter private String uri;
    @Getter @Setter private String body;
    @Getter @Setter private Http method;
    @Getter @Setter private Pagination pagination;

    /**
     * Default request evaluation. It is up to extending classes to override this method to provide a more thorough
     * validation check for each API's requirements.
     *
     * @return True if the request is valid, false otherwise
     */
    @Override
    public boolean validateRequest() {
        return true;
    }

    /**
     * Evaluates input {@link Predicate} of the input field provided it is present. Otherwise defaults to true.
     *
     * @param field Field
     * @param constraint {@link Predicate} of param field
     * @param <T> Any type
     * @return True if constraint of field is met or if field is not present, false otherwise
     */
    protected final <T> boolean ifPresent(final T field, final Predicate<T> constraint) {
        return field == null || constraint.test(field);
    }

    /**
     * Enforces input fields to all be present, or all be absent (null).
     *
     * @param fields Fields
     * @return True if fields are present, or if none are present
     */
    protected final boolean allOrNothing(final Object ... fields) {
        return fields == null || fields.length < 1
                // all fields must agree with the presence/absence of any other field
                || Arrays.stream(fields)
                    .allMatch(field -> field != null == (fields[0] != null));
    }
}
