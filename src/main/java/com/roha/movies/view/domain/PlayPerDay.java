package com.roha.movies.view.domain;

import com.roha.movies.domain.Play;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record PlayPerDay(DayOfWeek day, LocalDateTime start, String ticket, String cinema) {
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
    public PlayPerDay(Play play) {
        this(
            play.start().getDayOfWeek(),
            play.start(),
            play.tickethref(),
                play.cinema()
        );
    }

    @Override
    public String toString() {
        return format.format(start);
    }
}

