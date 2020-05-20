package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Model for adding new device to users account")
public class AddDeviceModel {
    @ApiModelProperty(notes = "The user account Id", example = "1234")
    @JsonSerialize
    @JsonProperty("userId")
    private int userId;

    @ApiModelProperty(notes = "The Id of the device that's being added", example = "5555")
    @JsonSerialize
    @JsonProperty("deviceId")
    private int deviceId;

    @ApiModelProperty(notes = "The device/room name(?)", example = "Kitchen")
    @JsonSerialize
    @JsonProperty("name")
    private String name;

    public AddDeviceModel() {
    }
}
