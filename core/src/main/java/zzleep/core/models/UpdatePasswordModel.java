package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Model for updating the user accounts password")
public class UpdatePasswordModel {
    @ApiModelProperty(notes = "The user account Id", example = "1234")
    @JsonSerialize
    @JsonProperty("userId")
    private int userId;

    @ApiModelProperty(notes = "The user accounts password before change")
    @JsonSerialize
    @JsonProperty("currentPassword")
    private String currentPassword;

    @ApiModelProperty(notes = "The new password that will be set if current password is correct")
    @JsonSerialize
    @JsonProperty("newPassword")
    private String newPassword;

    public UpdatePasswordModel() {
    }
}
