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
    private final int deviceId;
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
    private final int averageCo2;
    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("averageHumidity")
    private final int averageHumidity;
    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("averageSound")
    private final int averageSound;
    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("averageTemperature")
    private final int averageTemperature;

    public SleepSession(int sleepId, int deviceId, LocalDateTime timeStart, LocalDateTime timeFinish, int rating, int averageCo2, int averageHumidity, int averageSound, int averageTemperature) {
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

    public int getDeviceId() {
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

    public int getAverageCo2() {
        return averageCo2;
    }

    public int getAverageHumidity() {
        return averageHumidity;
    }

    public int getAverageSound() {
        return averageSound;
    }

    public int getAverageTemperature() {
        return averageTemperature;
    }
}
