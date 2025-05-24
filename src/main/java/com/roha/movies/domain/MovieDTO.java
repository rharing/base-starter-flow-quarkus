package com.roha.movies.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MovieDTO(String id, @JsonProperty("movie-id") String movieId, String title, String titleAddOn,
                       String image, String href, String rating, Date createdAt, Integer duration) {

    public MovieDTO withHref(String updatedHref){
        return new MovieDTO(id, movieId, title, titleAddOn, image, updatedHref, rating, createdAt, duration);
    }

    public MovieDTO withMovieId(String movieId) {
        return new MovieDTO(id, movieId, title, titleAddOn, image, href, rating, createdAt, duration);
    }

    public Movie asMovie() {
        return new Movie(id(), title(), href(), rating(), null, image(), duration(), new ArrayList<>(), titleAddOn());
    }
}
