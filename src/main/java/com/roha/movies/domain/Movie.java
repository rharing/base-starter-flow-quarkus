package com.roha.movies.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Builder
public record Movie(String id, String title, String href, String rating, String content, String imageHref,
                    Integer duration, @JsonIgnore List<Play> plays, String titleAddOn) {

    private static DateTimeFormatter TIMEFORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static DateTimeFormatter PLAY_ID_FORMATTER = DateTimeFormatter.ofPattern("MM-dd_HH:mm");
    private static DateTimeFormatter DATETIMEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


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
        return MovieDTO.builder()
                .id(id)
                .movieId(id)
                .title(title)
                .href(href)
                .rating(rating)
                .image(imageHref)
                .duration(duration)
                .titleAddOn(titleAddOn)
                .build();
    }

    public static class MovieBuilder {
        private List<Play> plays = new ArrayList<>();
    }

    public void addPlay(LocalDateTime startDate, String title, String href, String cinema, String titleAddOn) {
        Play play = Play.builder()
                .id(this.id() + "_"+ PLAY_ID_FORMATTER.format(startDate))
                .movie(this)
                .start(startDate)
                .End(startDate.plusMinutes(this.duration))
                .cinema(cinema)
                .tickethref(href)
                .titleAddOn(titleAddOn)
                .build();
        this.plays.add(play);
    }

    public Movie withDuration(LocalDateTime startDate, String titletimes) {
        String vanTijd = TIMEFORMATTER.format(startDate);
        String dateWithTime = DATETIMEFORMATTER.format(startDate);
        String totTijd = titletimes.split("Van " + vanTijd + " tot")[1];
        LocalDateTime eindTijd = startDate;
        try {
            String endTime = dateWithTime.replace(vanTijd, totTijd.strip());
            eindTijd = LocalDateTime.parse(endTime, DATETIMEFORMATTER);
        } catch (Exception e) {
            // hmm cant parse this so no way to calc the duration or the end time
        }

        Long duration = Duration.between(startDate, eindTijd).toMinutes();

        return Movie.builder()
                .duration(duration.intValue())
                .id(this.id)
                .content(this.content)
                .href(this.href)
                .rating(this.rating)
                .imageHref(this.imageHref)
                .plays(this.plays)
                .title(this.title).build();


    }
}

