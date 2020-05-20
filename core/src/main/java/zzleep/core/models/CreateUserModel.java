package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Model for creating user account")
public class CreateUserModel {

    @ApiModelProperty(notes = "The users email", example = "random_name@gmail.com")
    @JsonSerialize
    @JsonProperty("email")
    private String email;

    @ApiModelProperty(notes = "The users password")
    @JsonSerialize
    @JsonProperty("password")
    private String password;

    @ApiModelProperty(notes = "The users name", example = "Karen")
    @JsonSerialize
    @JsonProperty("name")
    private String name;

    public CreateUserModel() {
    }
}
