package com.roha.movies.domain;


import java.time.LocalDateTime;

public record Play(String id, Movie movie, LocalDateTime start, LocalDateTime End, String tickethref, String cinema, String titleAddOn) {

    public Play withStart(LocalDateTime localDateTime) {
        return new Play(this.id, this.movie, localDateTime, this.End, this.tickethref, this.cinema, this.titleAddOn);
    }

    public Boolean isOnTime(LocalDateTime now) {
return now.isBefore(start);
    }

    public PlayDTO asDTO() {
        return new PlayDTO(id, movie.asDTO(), start, End, tickethref, cinema, titleAddOn);
    }
}
