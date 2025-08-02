package com.roha.movies.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FilmLadderContent {
    @JsonProperty("@context")
    private String context;
    @JsonProperty("@type")
    private String type;
    private String url;
    private String name;
    private String description;
    private String datePublished;
    private String duration;
    private String image;
    @JsonProperty("director")
    ArrayList<Object> director = new ArrayList<Object>();
    @JsonProperty("actor")
    ArrayList<Object> actor = new ArrayList<Object>();

    AggregateRating AggregateRatingObject;


    // Getter Methods

    public String getContext() {
        return context;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public String getDuration() {
        return duration;
    }

    public int loadDuration(){
        if(this.duration != null){
            if(this.duration.startsWith("PT") && this.duration.endsWith("M")){
                this.duration = this.duration.replace("PT", "").replace("M", "");
            }
        }
        return Integer.parseInt(this.duration);
    }
    public String getImage() {
        return image;
    }

    public AggregateRating getAggregateRating() {
        return AggregateRatingObject;
    }

    // Setter Methods

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAggregateRating(AggregateRating aggregateRatingObject) {
        this.AggregateRatingObject = aggregateRatingObject;
    }
}

