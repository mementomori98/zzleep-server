package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Wrapper model for ideal room conditions")
public final class IdealRoomConditions {
    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("maxCo2")
    private final int maxCo2;

    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("minHumidity")
    private final double minHumidity;

    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("maxHumidity")
    private final double maxHumidity;

    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("minTemperature")
    private final double minTemperature;

    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("maxTemperature")
    private final double maxTemperature;

    public IdealRoomConditions(int maxCo2, double minHumidity, double maxHumidity, double minTemperature, double maxTemperature) {
        this.maxCo2 = maxCo2;
        this.minHumidity = minHumidity;
        this.maxHumidity = maxHumidity;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
    }

    public int getMaxCo2() {
        return maxCo2;
    }

    public double getMinHumidity() {
        return minHumidity;
    }

    public double getMaxHumidity() {
        return maxHumidity;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }
}
