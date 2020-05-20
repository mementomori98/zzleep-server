package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public final class IntervalReport {
    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("sleeps")
    private final List<SleepSession> sleeps;

    public IntervalReport(List<SleepSession> sleeps) {
        this.sleeps = sleeps;
    }

    public List<SleepSession> getSleeps() {
        return sleeps;
    }
}
