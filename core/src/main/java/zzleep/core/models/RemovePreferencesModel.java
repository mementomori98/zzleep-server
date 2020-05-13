package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Model for factory resetting device preferences(?)")
public class RemovePreferencesModel {
    @ApiModelProperty(notes = "The device Id", example = "1234")
    @JsonSerialize
    @JsonProperty("deviceId")
    private int deviceId;

    public RemovePreferencesModel() {
    }
}
