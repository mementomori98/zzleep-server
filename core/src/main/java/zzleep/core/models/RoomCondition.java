package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(description = "Wrapper model for room condition at a certain time")
public final class RoomCondition {
    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("sleepId")
    private final int sleepId;
    @ApiModelProperty(notes = "Timestamp of data provided")
    @JsonSerialize
    @JsonProperty("timestamp")
    private final LocalDateTime timestamp;
    @ApiModelProperty(notes = "Temperature in the room")
    @JsonSerialize
    @JsonProperty("temperature")
    private final double temperature;
    @ApiModelProperty(notes = "CO2 levels in the room")
    @JsonSerialize
    @JsonProperty("co2")
    private final double co2;
    @ApiModelProperty(notes = "Sound state in the room")
    @JsonSerialize
    @JsonProperty("sound")
    private final double sound;
    @ApiModelProperty(notes = "Humidity % of the room")
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

    public RoomCondition roomConditionCopy(){
        return new RoomCondition(this.sleepId, this.timestamp, this.temperature, this.co2, this.sound, this.humidity);
    }
}
