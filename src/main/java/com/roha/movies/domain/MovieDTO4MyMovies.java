package com.roha.movies.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO4MyMovies {

    @JsonProperty(value = "movie-id", required = true)
    String movieId;
    String title;
    String image;
    String content;
    String href;

    public MovieDTO asMovieDTO() {
        return new MovieDTO(movieId, movieId, title, "", image, href, "", new Date(), null);
    }
}
