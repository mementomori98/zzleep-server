package zzleep.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import zzleep.core.services.Response;

public abstract class ControllerBase {

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
            case CONFLICT:
                return custom(409);
            default:
                return custom(500);
        }
    }
}
