package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Wrapper model for the user account")
public final class User {
    @ApiModelProperty(notes = "The user account Id", example = "1234")
    @JsonSerialize
    @JsonProperty("userId")
    private final int userId;

    @ApiModelProperty(notes = "The users email", example = "random_name@gmail.com")
    @JsonSerialize
    @JsonProperty("email")
    private final String email;

    @ApiModelProperty(notes = "The users name", example = "Karen")
    @JsonSerialize
    @JsonProperty("name")
    private final String name;

    public User(int userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
