package com.roha.movies.view.domain;

import com.roha.movies.domain.Movie;
import com.roha.movies.domain.Play;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class PlayPerDayTest {

    @Test
    public void testHappyPath() {
        LocalDateTime start = LocalDateTime.of(2025, 6, 6, 13, 15, 0);
        LocalDateTime end = start.plusMinutes(90);
        Movie movie = new Movie("test", "movieTitle", "rating", "content", "href", "imageHref", 0, new ArrayList<>(), "");
        PlayPerDay playPerDay = new PlayPerDay(new Play("test", movie, start, end, "href", "cinema", "titleAddOn"));
//        assertThat(playPerDay.day()).is(start.getDayOfWeek());
//        assertThat(playPerDay.start()).is(start);
        assertThat(playPerDay.toString()).isEqualTo("13:15-14:45");
    }

}