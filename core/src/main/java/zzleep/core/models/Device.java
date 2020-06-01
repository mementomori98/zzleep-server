package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Holds data of a device")
public final class Device {

    @ApiModelProperty(example = "device1")
    @JsonSerialize
    @JsonProperty("deviceId")
    private String deviceId;

    @ApiModelProperty(example = "Parents' bedroom")
    @JsonSerialize
    @JsonProperty("name")
    private String name;

    @ApiModelProperty(example = "user1")
    @JsonSerialize
    @JsonProperty("userId")
    private String userId;

    public Device() {
    }

    public Device(String deviceId, String name, String userId) {
        this.deviceId = deviceId;
        this.name = name;
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Device)) return false;
        Device other = (Device) obj;
        return deviceId.equals(other.deviceId) &&
            name.equals(other.name) &&
            userId.equals(other.userId);
    }
}
