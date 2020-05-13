package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(description = "Wrapper model for sleep session")
public class SleepSession {
    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("sleepId")
    private final int sleepId;

    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("deviceId")
    private final String deviceId;

    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("timeStart")
    private final LocalDateTime timeStart;

    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("timeFinish")
    private final LocalDateTime timeFinish;

    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("rating")
    private final int rating;

    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("averageCo2")
    private final double averageCo2;

    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("averageHumidity")
    private final double averageHumidity;

    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("averageSound")
    private final double averageSound;

    @ApiModelProperty
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
