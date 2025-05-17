package com.roha.movies.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public record MovieDTO4MyMovies(@JsonProperty(value = "movie-id", required = true) String movieId, String title, String image, String content, String href) {

    public MovieDTO asMovieDTO() {
        return new MovieDTO(movieId, movieId, title, "", image, href, "", new Date(), null);
    }
}
