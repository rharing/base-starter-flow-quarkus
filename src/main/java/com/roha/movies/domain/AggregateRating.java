package com.roha.movies.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AggregateRating {
    @JsonProperty("@type")
    private String type;
    private float ratingValue;
    private String bestRating;
    private String worstRating;
    private float ratingCount;


    // Getter Methods

    public String getType() {
        return type;
    }

    public float getRatingValue() {
        return ratingValue;
    }

    public String getBestRating() {
        return bestRating;
    }

    public String getWorstRating() {
        return worstRating;
    }

    public float getRatingCount() {
        return ratingCount;
    }

    // Setter Methods

    public void setType(String type) {
        this.type = type;
    }

    public void setRatingValue(float ratingValue) {
        this.ratingValue = ratingValue;
    }

    public void setBestRating(String bestRating) {
        this.bestRating = bestRating;
    }

    public void setWorstRating(String worstRating) {
        this.worstRating = worstRating;
    }

    public void setRatingCount(float ratingCount) {
        this.ratingCount = ratingCount;
    }
}
