package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Holds the data necessary to add a device to a user")
public class AddDeviceModel {

    @ApiModelProperty(example = "device1")
    @JsonSerialize
    @JsonProperty("deviceId")
    private String deviceId;

    @ApiModelProperty(example = "user1")
    @JsonSerialize
    @JsonProperty("userId")
    private String userId;

    @ApiModelProperty(example = "Parents' Bedroom")
    @JsonSerialize
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
