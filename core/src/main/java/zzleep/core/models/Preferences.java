package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Wrapper model for preferences of device")
public final class Preferences {
    @ApiModelProperty(notes = "The device Id of which preferences are given", example = "1234")
    @JsonSerialize
    @JsonProperty("deviceId")
    private String deviceId;

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
    private double humidityMin;

    @ApiModelProperty(notes = "Maximum tolerable amount of humidty", example = "69420")
    @JsonSerialize
    @JsonProperty("humidityMax")
    private double humidityMax;

    @ApiModelProperty(notes = "Lowest point of tolerable temperature", example = "15.0")
    @JsonSerialize
    @JsonProperty("temperatureMin")
    private int temperatureMin;

    @ApiModelProperty(notes = "Highest point of tolerable temperature", example = "25.0")
    @JsonSerialize
    @JsonProperty("temperatureMax")
    private int temperatureMax;

    public Preferences(String deviceId, boolean regulationEnabled, int co2Max, double humidityMin, double humidityMax, int temperatureMin, int temperatureMax) {
        this.deviceId = deviceId;
        this.regulationEnabled = regulationEnabled;
        this.co2Max = co2Max;
        this.humidityMin = humidityMin;
        this.humidityMax = humidityMax;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
    }

    public Preferences() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public boolean isRegulationEnabled() {
        return regulationEnabled;
    }

    public int getCo2Max() {
        return co2Max;
    }

    public double getHumidityMin() {
        return humidityMin;
    }

    public double getHumidityMax() {
        return humidityMax;
    }

    public int getTemperatureMin() {
        return temperatureMin;
    }

    public int getTemperatureMax() {
        return temperatureMax;
    }

    @JsonIgnore
    public boolean isValid() {
        return temperatureMin < temperatureMax &&
            humidityMin < humidityMax;
    }
}
