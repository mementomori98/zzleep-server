package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel
public class SleepData extends SleepSession {
    @ApiModelProperty
    @JsonSerialize
    @JsonProperty
    private List<RoomCondition> roomConditions;

    public SleepData(int sleepId, String deviceId, LocalDateTime timeStart, LocalDateTime timeFinish, int rating, double averageCo2, double averageHumidity, double averageSound, double averageTemperature, List<RoomCondition> roomConditions) {
        super(sleepId, deviceId, timeStart, timeFinish, rating, averageCo2, averageHumidity, averageSound, averageTemperature);
        this.roomConditions = roomConditions;
    }

    public List<RoomCondition> getRoomConditions() {
        return roomConditions;
    }
}
