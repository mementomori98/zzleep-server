package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(description = "Represents the sleeping environment at a given time")
public final class RoomCondition {

    @ApiModelProperty(example = "3")
    @JsonSerialize
    @JsonProperty("sleepId")
    private final int sleepId;

    @ApiModelProperty(example = "2020-05-21T13:48:16.141Z")
    @JsonSerialize
    @JsonProperty("timestamp")
    private final LocalDateTime timestamp;

    @ApiModelProperty(example = "32.42", notes = "Represented in Celsius, rounded to 2 decimal digits")
    @JsonSerialize
    @JsonProperty("temperature")
    private final double temperature;

    @ApiModelProperty(notes = "Represented in ppm, rounded to 2 decimal digits", example = "553")
    @JsonSerialize
    @JsonProperty("co2")
    private final double co2;

    @ApiModelProperty(notes = "Represented in dB, rounded to 2 decimal digits", example = "42.16")
    @JsonSerialize
    @JsonProperty("sound")
    private final double sound;

    @ApiModelProperty(notes = "Represented in %, rounded to 2 decimal digits", example = "42.27")
    @JsonSerialize
    @JsonProperty("humidity")
    private final double humidity;

    public RoomCondition(int sleepId, LocalDateTime timestamp, double temperature, double co2, double sound, double humidity) {
        this.sleepId = sleepId;
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.co2 = co2;
        this.sound = sound;
        this.humidity = humidity;
    }

    public int getSleepId() {
        return sleepId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getTemperature() {
        return temperature;
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

    public RoomCondition roomConditionCopy() {
        return new RoomCondition(this.sleepId, this.timestamp, this.temperature, this.co2, this.sound, this.humidity);
    }
}
