package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Model for regualtions(?)")
public final class RegulationGuide {
    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("temperature")
    private final int temperature;

    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("co2")
    private final int co2;

    @ApiModelProperty
    @JsonSerialize
    @JsonProperty("humidity")
    private final int humidity;

    public RegulationGuide(int temperature, int co2, int humidity) {
        this.temperature = temperature;
        this.co2 = co2;
        this.humidity = humidity;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getCo2() {
        return co2;
    }

    public int getHumidity() {
        return humidity;
    }
}
