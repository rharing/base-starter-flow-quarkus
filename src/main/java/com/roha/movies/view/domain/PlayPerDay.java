package com.roha.movies.view.domain;

import com.roha.movies.domain.Play;
import java.time.LocalDate;

public record PlayPerDay(String day, LocalDate date, String ticket,String cinema) {
    public PlayPerDay(Play play) {
        this(
            play.start().getDayOfWeek().toString(),
            play.start().toLocalDate(),
            play.tickethref(),
                play.cinema()
        );
    }
}

