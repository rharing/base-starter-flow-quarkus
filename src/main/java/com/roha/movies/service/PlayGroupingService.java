package com.roha.movies.service;

import com.roha.movies.domain.Movie;
import com.roha.movies.domain.Play;
import com.roha.movies.view.domain.PlayPerDay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PlayGroupingService {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE");

    public PlayGroupingResult groupPlaysByDay(Movie movie) {
        return groupPlaysByDay(movie, LocalDateTime.now());
    }

        public PlayGroupingResult groupPlaysByDay(Movie movie, LocalDateTime now) {
        Set<String> cinemas = new HashSet<>();
        Map<String, List<PlayPerDay>> playsByDay = new HashMap<>();
        playsByDay.put("maandag", new ArrayList<>());
        playsByDay.put("dinsdag", new ArrayList<>());
        playsByDay.put("woensdag", new ArrayList<>());
        playsByDay.put("donderdag", new ArrayList<>());
        playsByDay.put("vrijdag", new ArrayList<>());
        playsByDay.put("zaterdag", new ArrayList<>());
        playsByDay.put("zondag", new ArrayList<>());
        movie.plays().forEach(play -> {
            cinemas.add(play.cinema());
            PlayPerDay playPerDay = new PlayPerDay(play);

            String dayOfWeek = formatter.format(play.start());
            playsByDay.computeIfAbsent(dayOfWeek, k -> new ArrayList<>()).add(playPerDay);
        });

        String today = formatter.format(now);
        String morgen = formatter.format(now.plusDays(1));


        playsByDay.put("vandaag", playsByDay.get(today));
        playsByDay.remove(today);
        playsByDay.put("morgen", playsByDay.get(morgen));
        playsByDay.remove(morgen);

        return new PlayGroupingResult(cinemas, playsByDay);
    }
    
    public record PlayGroupingResult(Set<String> cinemas, Map<String, List<PlayPerDay>> playsByDay) {}
}
