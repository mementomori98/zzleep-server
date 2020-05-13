package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Wrapper model for a device")
public final class Device {
    @ApiModelProperty(notes = "The device Id", example = "1234")
    @JsonSerialize
    @JsonProperty("deviceId")
    private final int deviceId;

    @ApiModelProperty(notes = "The device/room name(?)", example = "Kitchen")
    @JsonSerialize
    @JsonProperty("name")
    private final String name;

    public Device(int deviceId, String name) {
        this.deviceId = deviceId;
        this.name = name;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public String getName() {
        return name;
    }
}
