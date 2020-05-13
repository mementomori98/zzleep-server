package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Model for updating existing device in users account")
public class UpdateDeviceModel {
    @ApiModelProperty(notes = "The Id of the device that's being updated", example = "5555")
    @JsonSerialize
    @JsonProperty("deviceId")
    private int deviceId;

    @ApiModelProperty(notes = "The new(?) device/room name(?)", example = "Kitchen")
    @JsonSerialize
    @JsonProperty("name")
    private String name;

    public UpdateDeviceModel() {
    }
}
