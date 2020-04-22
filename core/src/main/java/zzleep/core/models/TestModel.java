package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "A test model for the Zzleep API")
public class TestModel {

    @ApiModelProperty(notes = "The message retrieved from the api", example = "Hello World")
    @JsonSerialize
    @JsonProperty("message")
    private String message = "Hello World";

    public TestModel(String message) {
        this.message = message;
    }

}
