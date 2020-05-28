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

    @ApiModelProperty(example = "Tuck, 2020")
    @JsonSerialize
    @JsonProperty("source")
    private final String source;

    @ApiModelProperty(example = "https://www.tuck.com/rem-sleep/")
    @JsonSerialize
    @JsonProperty("sourceUrl")
    private final String sourceUrl;

    public Fact(int id, String title, String content, String source, String sourceUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.source = source;
        this.sourceUrl = sourceUrl;
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

    public String getSource() {
        return source;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }
}
