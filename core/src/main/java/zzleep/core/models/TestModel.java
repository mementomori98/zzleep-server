package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "A test model for the Zzleep API")
public class TestModel {

    private int id;

    @ApiModelProperty(notes = "The message retrieved from the api", example = "Hello World")
    @JsonSerialize
    @JsonProperty("message")
    private String message = "Hello World";

    public TestModel() {

    }

    public TestModel(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
