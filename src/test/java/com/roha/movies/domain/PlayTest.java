package com.roha.movies.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class PlayTest {

    @Test
    public void shouldBeIsOnTime(){
        LocalDateTime now = LocalDateTime.of(2024, Month.APRIL, 20, 17, 0, 0);
        Movie movie = new Movie("123", "title", "image", "8.5", "content", "href", 42, new ArrayList<>(),"");
        Play play = new Play("123", movie, now, now, "href", "cinema", "titleAddOn");
        Play playAlreadyStarted = play.withStart(now.minusMinutes(1));
        Play futurePlay = play.withStart(now.plusMinutes(1));
        assertThat(playAlreadyStarted.isOnTime(now)).isFalse();
        assertThat(futurePlay.isOnTime(now)).isTrue();
    }

}