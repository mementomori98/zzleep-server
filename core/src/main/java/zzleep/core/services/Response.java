package zzleep.core.services;

public class Response<TType> {

    private Status status;
    private TType model;

    public Response(Status status) {
        this.status = status;
    }

    public Response(Status status, TType model) {
        this.status = status;
        this.model = model;
    }

    public Status getStatus() {
        return status;
    }

    public TType getModel() {
        return model;
    }
}
