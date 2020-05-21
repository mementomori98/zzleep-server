package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Holds data of a fact about sleep")
public final class Fact {

    @ApiModelProperty(example = "12")
    @JsonSerialize
    @JsonProperty("id")
    private final int id;

    @ApiModelProperty(example = "Eating before going to bed")
    @JsonSerialize
    @JsonProperty("title")
    private final String title;

    @ApiModelProperty(example = "Never eat before going to sleep.")
    @JsonSerialize
    @JsonProperty("content")
    private final String content;

    public Fact(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
