package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(description = "Wrapper model for sleep session")
public class SleepSession {
    @ApiModelProperty(example = "5")
    @JsonSerialize
    @JsonProperty("sleepId")
    private final int sleepId;

    @ApiModelProperty(example = "device1")
    @JsonSerialize
    @JsonProperty("deviceId")
    private final String deviceId;

    @ApiModelProperty(example = "2020-02-27T22:40:03.887Z")
    @JsonSerialize
    @JsonProperty("timeStart")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timeStart;

    @ApiModelProperty(example = "2020-02-28T07:20:00.247Z")
    @JsonSerialize
    @JsonProperty("timeFinish")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timeFinish;

    @ApiModelProperty(example = "4")
    @JsonSerialize
    @JsonProperty("rating")
    private final int rating;

    @ApiModelProperty(notes = "Value is represented in ppm, rounded to 2 decimal digits", example = "521.42")
    @JsonSerialize
    @JsonProperty("averageCo2")
    private final double averageCo2;

    @ApiModelProperty(notes = "Value is represented in percentage, rounded to 2 decimal digits", example = "54.32")
    @JsonSerialize
    @JsonProperty("averageHumidity")
    private final double averageHumidity;

    @ApiModelProperty(notes = "Value is represented in dB, rounded to 2 decimal digits", example = "45.12")
    @JsonSerialize
    @JsonProperty("averageSound")
    private final double averageSound;

    @ApiModelProperty(notes = "Value is represented in Celsius, rounded to 2 decimal digits", example = "22.02")
    @JsonSerialize
    @JsonProperty("averageTemperature")
    private final double averageTemperature;

    public SleepSession(
        int sleepId,
        String deviceId,
        LocalDateTime timeStart,
        LocalDateTime timeFinish,
        int rating,
        double averageCo2,
        double averageHumidity,
        double averageSound,
        double averageTemperature
    ) {

        this.sleepId = sleepId;
        this.deviceId = deviceId;
        this.timeStart = timeStart;
        this.timeFinish = timeFinish;
        this.rating = rating;
        this.averageCo2 = averageCo2;
        this.averageHumidity = averageHumidity;
        this.averageSound = averageSound;
        this.averageTemperature = averageTemperature;
    }

    public int getSleepId() {
        return sleepId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public LocalDateTime getTimeFinish() {
        return timeFinish;
    }

    public int getRating() {
        return rating;
    }

    public double getAverageCo2() {
        return averageCo2;
    }

    public double getAverageHumidity() {
        return averageHumidity;
    }

    public double getAverageSound() {
        return averageSound;
    }

    public double getAverageTemperature() {
        return averageTemperature;
    }
}
