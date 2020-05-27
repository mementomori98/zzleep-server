package zzleep.core.models;

public class AuthObject {

    private String deviceId;
    private String userId;
    private int sleepId;

    public AuthObject(String deviceId, String userId, int sleepId) {
        this.deviceId = deviceId;
        this.userId = userId;
        this.sleepId = sleepId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public int getSleepId() {
        return sleepId;
    }
}
