package com.roha.movies.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 */
public record Movie(String id, String title, String href, String rating, String content, String imageHref,
                    Integer duration, @JsonIgnore List<Play> plays, String titleAddOn) {


    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", href='" + href + '\'' +
                ", rating='" + rating + '\'' +
                ", content='" + content + '\'' +
                ", imageHref='" + imageHref + '\'' +
                ", duration=" + duration +
                '}';
    }

    public MovieDTO asDTO() {
        return new MovieDTO(id,id, title, titleAddOn, imageHref, href, rating, null, duration);
    }

    public void addPlay(LocalDateTime startDate, String title, String href, String cinema, String titleAddOn) {
        String playId = this.id() + "_" + Formatters.PLAY_ID_FORMATTER.format(startDate);
        Play play = new Play(playId, this, startDate, startDate.plusMinutes(this.duration), href, cinema, titleAddOn);
        this.plays.add(play);
    }

    public Movie withDuration(LocalDateTime startDate, String titletimes) {
        String vanTijd = Formatters.TIMEFORMATTER.format(startDate);
        String dateWithTime = Formatters.DATETIMEFORMATTER.format(startDate);
        String totTijd = titletimes.split("Van " + vanTijd + " tot")[1];
        LocalDateTime eindTijd = startDate;
        try {
            String endTime = dateWithTime.replace(vanTijd, totTijd.strip());
            eindTijd = LocalDateTime.parse(endTime, Formatters.DATETIMEFORMATTER);
        } catch (Exception e) {
            // hmm cant parse this so no way to calc the duration or the end time
        }

        Long duration = Duration.between(startDate, eindTijd).toMinutes();

        return new Movie(this.id, this.title, this.href, this.rating, this.content, this.imageHref, duration.intValue(), this.plays, this.titleAddOn);
    }
}

