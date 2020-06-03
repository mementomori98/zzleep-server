package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(description = "Represents the sleeping environment at a given time")
public class RoomCondition {

    @ApiModelProperty(example = "3")
    @JsonSerialize
    @JsonProperty("sleepId")
    private int sleepId;

    @ApiModelProperty(example = "2020-05-21T13:48:16.141Z")
    @JsonSerialize
    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @ApiModelProperty(example = "32.42", notes = "Represented in Celsius, rounded to 2 decimal digits")
    @JsonSerialize
    @JsonProperty("temperature")
    private int temperature;

    @ApiModelProperty(notes = "Represented in ppm, rounded to 2 decimal digits", example = "553")
    @JsonSerialize
    @JsonProperty("co2")
    private int co2;

    @ApiModelProperty(notes = "Represented in dB, rounded to 2 decimal digits", example = "42.16")
    @JsonSerialize
    @JsonProperty("sound")
    private double sound;

    @ApiModelProperty(notes = "Represented in %, rounded to 2 decimal digits", example = "42.27")
    @JsonSerialize
    @JsonProperty("humidity")
    private double humidity;

    public RoomCondition() {
    }

    public RoomCondition(int sleepId, LocalDateTime timestamp, int temperature, int co2, double sound, double humidity) {
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

    public int getTemperature() {
        return temperature;
    }

    public int getCo2() {
        return co2;
    }

    public double getSound() {
        return sound;
    }

    public double getHumidity() {
        return humidity;
    }

}
