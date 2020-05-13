package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@ApiModel(description = "Model for sleep")
public class Sleep {
    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("sleepId")
    private int sleepId;
    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("deviceId")
    private String deviceId;
    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("dateTimeStart")
    private LocalDateTime dateTimeStart;
    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("dateTimeFinish")
    private LocalDateTime dateTimeFinish;
    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("rating")
    private int rating;

    public Sleep(int sleepId, String deviceId, LocalDateTime dateTimeStart, LocalDateTime dateTimeFinish, int rating) {
        this.sleepId = sleepId;
        this.deviceId = deviceId;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeFinish = dateTimeFinish;
        this.rating = rating;
    }

    public Sleep(String deviceId, LocalDateTime dateTimeStart) {
        this.deviceId = deviceId;
        this.dateTimeStart = dateTimeStart;
    }

    public int getSleepId() {
        return sleepId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public LocalDateTime getDateTimeStart() {
        return dateTimeStart;
    }

    public LocalDateTime getDateTimeFinish() {
        return dateTimeFinish;
    }

    public int getRating() {
        return rating;
    }

    public void setSleepId(int sleepId) {
        this.sleepId = sleepId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setDateTimeStart(LocalDateTime dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public void setDateTimeFinish(LocalDateTime dateTimeFinish) {
        this.dateTimeFinish = dateTimeFinish;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
