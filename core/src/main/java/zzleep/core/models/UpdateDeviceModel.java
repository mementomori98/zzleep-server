package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Holds data necessary to update a device")
public class UpdateDeviceModel {
    @ApiModelProperty(example = "device1")
    @JsonSerialize
    @JsonProperty("deviceId")
    private String deviceId;

    @ApiModelProperty(example = "Parents' bedroom")
    @JsonSerialize
    @JsonProperty("name")
    private String name;

    public UpdateDeviceModel() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getName() {
        return name;
    }
}
