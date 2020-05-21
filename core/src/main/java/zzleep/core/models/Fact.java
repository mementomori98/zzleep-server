package zzleep.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Wrapper model for facts & tips")
public final class Fact {

    @ApiModelProperty(notes = "The id of the given fact", example = "10")
    @JsonSerialize
    @JsonProperty("id")
    private final int id;

    @ApiModelProperty(notes = "The title of the given fact", example = "Sleep well 069")
    @JsonSerialize
    @JsonProperty("title")
    private final String title;

    @ApiModelProperty(notes = "The content of the given fact", example = "Never eat before going to sleep as this dra...")
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
