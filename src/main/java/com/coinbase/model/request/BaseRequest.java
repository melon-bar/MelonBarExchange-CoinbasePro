package com.coinbase.model.request;

import com.coinbase.client.http.Http;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Predicate;

public abstract class BaseRequest implements Request {

    @Getter @Setter private String uri;
    @Getter @Setter private Http method;
    @Getter @Setter private String body;

    protected final <T> boolean ifPresent(final T field, final Predicate<T> constraint) {
        return field == null || constraint.test(field);
    }

    @Override
    public boolean validateRequest() {
        return true;
    }
}
