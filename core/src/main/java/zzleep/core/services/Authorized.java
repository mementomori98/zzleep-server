package zzleep.core.services;

public class Authorized<TType> {

    private String userId;
    private TType model;

    public Authorized(String userId) {
        this.userId = userId;
    }

    public Authorized(String userId, TType model) {
        this.userId = userId;
        this.model = model;
    }

    public String getUserId() {
        return userId;
    }

    public TType getModel() {
        return model;
    }
}
