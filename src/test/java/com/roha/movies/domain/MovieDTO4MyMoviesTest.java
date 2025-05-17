package com.roha.movies.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MovieDTO4MyMoviesTest {

    @Test
    public void bugAsDTO(){
        MovieDTO4MyMovies dto = new MovieDTO4MyMovies("movieId","title","image","content","href");
        MovieDTO movieDTO = dto.asMovieDTO();
        assertThat(movieDTO.movieId()).isEqualTo("movieId");
        assertThat(movieDTO.title()).isEqualTo("title");
        assertThat(movieDTO.image()).isEqualTo("image");
        assertThat(movieDTO.href()).isEqualTo("href");
        assertThat(movieDTO.rating()).isEqualTo("");
    }

}