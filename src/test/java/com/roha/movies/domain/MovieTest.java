package com.roha.movies.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class MovieTest {

    @Test
    void tooLate() {
        Movie movie = new Movie("someMovie", "someMovie", "someMovie", "someMovie", "someMovie", "someMovie", 25, new ArrayList<>(), "");
        LocalDateTime when = LocalDateTime.of(2020, 4, 20, 18, 20);
        movie.addPlay(when.minusMinutes(3), "firstPlay","firstPlay","schuur", "");
        assertThat(movie.plays().get(0).isOnTime(when)).isFalse();
        movie.addPlay(when.plusMinutes(3), "secondPlay","secondPlay","schuur", "");
        assertThat(movie.plays().get(1).isOnTime(when)).isTrue();
        assertThat(movie.plays()).hasSize(2);
        assertThat(movie.plays().get(0).id()).isEqualTo("someMovie_04-20_18:17");
        assertThat(movie.plays().get(1).id()).isEqualTo("someMovie_04-20_18:23");
    }

    @Test
    void asDto(){
        Movie movie = new Movie("someMovieId", "someMovieTitle", "href", "rating", "", "", 25, new ArrayList<>(), "");

        MovieDTO dto = movie.asDTO();
        assertThat(dto.movieId()).isEqualTo("someMovieId");
        assertThat(dto.title()).isEqualTo("someMovieTitle");
        assertThat(dto.duration()).isEqualTo(25);
        assertThat(dto.href()).isEqualTo("href");
        assertThat(dto.rating()).isEqualTo("rating");
    }
}