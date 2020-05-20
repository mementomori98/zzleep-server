package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Model for setting preferences of your own")
public class SetPreferencesModel {
    @ApiModelProperty(notes = "The device Id of which preferences will be set", example = "1234")
    @JsonSerialize
    @JsonProperty("deviceId")
    private int deviceId;

    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("regulationEnabled")
    private boolean regulationEnabled;

    @ApiModelProperty(notes = "Maximum tolerable amount of CO2 allowed", example = "69420")
    @JsonSerialize
    @JsonProperty("co2Max")
    private int co2Max;

    @ApiModelProperty(notes = "Minimum tolerable amount of humidity", example = "69420")
    @JsonSerialize
    @JsonProperty("humidityMin")
    private int humidityMin;

    @ApiModelProperty(notes = "Maximum tolerable amount of humidty", example = "69420")
    @JsonSerialize
    @JsonProperty("humidityMax")
    private int humidityMax;

    @ApiModelProperty(notes = "Lowest point of tolerable temperature", example = "15.0")
    @JsonSerialize
    @JsonProperty("temperatureMin")
    private double temperatureMin;

    @ApiModelProperty(notes = "Highest point of tolerable temperature", example = "25.0")
    @JsonSerialize
    @JsonProperty("temperatureMax")
    private double temperatureMax;

    public SetPreferencesModel() {
    }
}
