package com.makeuseof.muocore.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by abaikirov on 10/3/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)

public class PublisherImage {

    @JsonProperty("featured")
    public String featured;

    @JsonProperty("thumb")
    public String thumb;

    @JsonProperty("middle")
    public String middle;

}
