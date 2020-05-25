package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Holds data that represents the ideal room conditions")
public final class IdealRoomConditions {

    @ApiModelProperty(notes = "Unit is ppm", example = "552")
    @JsonSerialize
    @JsonProperty("co2")
    private final double co2;

    @ApiModelProperty(notes = "Unit is dB", example = "45")
    @JsonSerialize
    @JsonProperty("sound")
    private final double sound;

    @ApiModelProperty (notes = "Unit is %", example = "60")
    @JsonSerialize
    @JsonProperty("humidity")
    private final double humidity;


    @ApiModelProperty(notes = "Unit is degrees Celsius", example = "21")
    @JsonSerialize
    @JsonProperty("temperature")
    private final double temperature;

    public IdealRoomConditions(double co2, double sound, double humidity, double temperature) {
        this.co2 = co2;
        this.sound = sound;
        this.humidity = humidity;
        this.temperature = temperature;
    }

    public double getCo2() {
        return co2;
    }

    public double getSound() {
        return sound;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getTemperature() {
        return temperature;
    }
}
