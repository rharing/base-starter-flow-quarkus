package com.roha.movies.view.domain;

import com.roha.movies.domain.Play;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WeekOverView {
    private List<DayOverview> days = new ArrayList<>();
    private Set<String> cinemas = new HashSet<>();
    // EEE gives short day names, EEEE would be full length.
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE", Locale.US);

    public WeekOverView() {
        Locale locale = Locale.forLanguageTag("nl");
        DayOfWeek firstDayOfWeek = WeekFields.of(locale).getFirstDayOfWeek();

        List<DayOfWeek> dows = IntStream.range(0, 7)
                .mapToObj(firstDayOfWeek::plus)
                .collect(Collectors.toList());
        for (DayOfWeek day : dows) {
            DayOverview dayOverview = new DayOverview(day);
            days.add(dayOverview);
            dayOverview.setDag(day.getDisplayName(TextStyle.FULL, locale));
        }
    }

    public void add(PlayPerDay playPerDay) {
        int dayofWeek = playPerDay.day().getValue();
        if(dayofWeek== 7) {
            dayofWeek = 0;
        }
        days.get(dayofWeek).add(playPerDay);
        cinemas.add(playPerDay.cinema());
    }

    public List<DayOverview> getDays() {
        return days;
    }

    public Set<String> getCinemas() {
        return cinemas;
    }

    public void addPlays(List<Play> plays) {
        for (Play play : plays) {
            add(new PlayPerDay(play));
        }
    }

    public List<DayOverview> getOverview(LocalDate now) {
        DayOfWeek today = now.getDayOfWeek();
        // start with today
        int start = today.getValue();
        List<DayOverview> overview = new ArrayList<>();
        overview = this.days.subList(start, 7);
        overview.addAll(this.days.subList(0, start));
        overview.get(0).setDag("vandaag");
        overview.get(1).setDag("morgen");
        return overview;
    }
}