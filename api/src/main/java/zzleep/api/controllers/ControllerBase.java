package zzleep.api.controllers;

import org.springframework.http.ResponseEntity;

public abstract class ControllerBase {

    protected static final String NOT_FOUND_MESSAGE = "The resource could not be found";

    protected <TType> ResponseEntity<TType> custom(int status) {
        return custom(status, null);
    }

    protected <TType> ResponseEntity<TType> custom(int status, TType body) {
        if (body == null)
            return ResponseEntity
                .status(status)
                .build();

        return ResponseEntity
            .status(status)
            .body(body);
    }

    protected <TType> ResponseEntity<TType> success(TType body) {
        return custom(200, body);
    }

    protected <TType> ResponseEntity<TType> success() {
        return custom(200);
    }

    protected <TType> ResponseEntity<TType> notFound() {
        return custom(404);
    }

    protected <TType> ResponseEntity<TType> notFound(TType body) {
        return custom(404, body);
    }
}
