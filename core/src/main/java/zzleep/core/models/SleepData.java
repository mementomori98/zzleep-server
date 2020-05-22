package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApiModel(description = "A model containing all data collected for a sleep")
public class SleepData extends SleepSession {

    @ApiModelProperty
    @JsonSerialize
    @JsonProperty
    private List<RoomCondition> roomConditions;

    public SleepData(int sleepId, String deviceId, LocalDateTime timeStart, LocalDateTime timeFinish, int rating, List<RoomCondition> roomConditions) {
        super(
            sleepId, deviceId, timeStart, timeFinish, rating,
            calculateAverage(RoomCondition::getCo2, roomConditions),
            calculateAverage(RoomCondition::getHumidity, roomConditions),
            calculateAverage(RoomCondition::getSound, roomConditions),
            calculateAverage(RoomCondition::getTemperature, roomConditions)
        );
        this.roomConditions = roomConditions;
    }

    public List<RoomCondition> getRoomConditions() {
        return roomConditions;
    }

    private static <TType extends Number> double calculateAverage(
        Function<RoomCondition, TType> mapper,
        List<RoomCondition> roomConditions
    ) {
        double sum = 0;
        List<TType> list = roomConditions.stream().map(mapper).collect(Collectors.toList());
        for (TType value : list)
            sum += value.doubleValue();
        return sum / list.size();
    }
}