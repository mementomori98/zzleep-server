package zzleep.core.services;

import static zzleep.core.services.Status.*;

public class ServiceBase {

    protected<TType> Response<TType> success() {
        return custom(SUCCESS);
    }

    protected<TType> Response<TType> success(TType model) {
        return custom(SUCCESS, model);
    }

    protected <TType> Response<TType> noContent() {
        return custom(NO_CONTENT);
    }

    protected <TType> Response<TType> unauthorized() {
        return custom(UNAUTHORIZED);
    }

    protected <TType> Response<TType> notAllowed() {
        return custom(NOT_ALLOWED);
    }

    protected <TType> Response<TType> notFound() {
        return custom(NOT_FOUND);
    }

    private <TType> Response<TType> custom(Status status) {
        return new Response<>(status);
    }

    private <TType> Response<TType> custom(Status status, TType body) {
        return new Response<>(status, body);
    }

    protected <TType> Response<TType> conflict() {
        return custom(CONFLICT);
    }
}
