package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddDeviceModel {

    @JsonProperty("deviceId")
    private String deviceId;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("name")
    private String name;

    public String getDeviceId() {
        return deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
