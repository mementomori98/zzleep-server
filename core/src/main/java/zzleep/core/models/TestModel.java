package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TestModel {

    @JsonSerialize
    @JsonProperty("message")
    private String message = "Hello World";

}
