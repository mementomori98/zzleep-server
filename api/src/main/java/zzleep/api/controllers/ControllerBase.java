package zzleep.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import zzleep.core.services.Response;

import static zzleep.core.services.Status.*;

public abstract class ControllerBase {

    protected static final String NOT_FOUND_MESSAGE = "The resource could not be found";

    protected String userId() {
        return SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getName();
    }

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

    protected <TType> ResponseEntity<TType> forbidden() {
        return custom(403);
    }

    protected <TType> ResponseEntity<TType> badRequest() {
        return custom(400);
    }

    protected <TType> ResponseEntity<TType> successOrNotFound(TType object) {
        return object == null ? notFound() : success(object);
    }

    protected <TType> ResponseEntity<TType> error() {
        return custom(500);
    }

    protected <TType> ResponseEntity<TType> map(Response<TType> response) {
        switch (response.getStatus()) {
            case SUCCESS:
                return custom(200, response.getModel());
            case NO_CONTENT:
                return custom(204);
            case UNAUTHORIZED:
                return custom(403);
            case NOT_ALLOWED:
                return custom(406);
            case NOT_FOUND:
                return custom(404);
            default:
                return custom(500);
        }
    }
}
