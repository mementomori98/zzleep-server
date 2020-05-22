package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@ApiModel(description = "A model representing a sleep")
public class Sleep {

    @ApiModelProperty(example = "2")
    @JsonSerialize
    @JsonProperty("sleepId")
    private int sleepId;

    @ApiModelProperty(example = "device2")
    @JsonSerialize
    @JsonProperty("deviceId")
    private String deviceId;

    @ApiModelProperty(example = "2020-05-21T13:48:16.141Z")
    @JsonSerialize
    @JsonProperty("dateTimeStart")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTimeStart;

    @ApiModelProperty(example = "2020-06-21T13:48:16.141Z")
    @JsonSerialize
    @JsonProperty("dateTimeFinish")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTimeFinish;

    @ApiModelProperty(example = "3")
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
