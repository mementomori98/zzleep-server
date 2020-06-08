package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(description = "Summarizes data for sleepSessions in a given time period")
public final class IntervalReport {

    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("sleepSessions")
    private final List<SleepSession> sleepSessions;

    public IntervalReport(List<SleepSession> sleepSessions) {
        this.sleepSessions = sleepSessions;
        this.sleepSessions.sort((o1, o2) -> o1.getTimeStart().isAfter(o2.getTimeStart()) ? -1 : 1);
    }

    public List<SleepSession> getSleepSessions() {
        return new ArrayList<>(sleepSessions);
    }
}
