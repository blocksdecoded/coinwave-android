package com.makeuseof.muocore.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by abaikirov on 10/3/17.
 */


@JsonIgnoreProperties(ignoreUnknown = true)

public class PublisherPost {

    private static SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @JsonProperty("ID")
    public int id;

    public Date date;

    @JsonSetter("post_date")
    public void setPostDate(final String postDate){
        try{
            this.date = date_format.parse(postDate);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @JsonProperty("post_title")
    public String title;

    @JsonProperty("author")
    public String author;

    @JsonProperty("html")
    public String html;

    @JsonProperty("url")
    public String url;

    @JsonProperty("featured_image")
    public PublisherImage image;

}
