package com.roha.movies.view.domain;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class DayOverview {
    private final DayOfWeek day;
    private List<PlayPerDay> plays = new ArrayList<>();
    private String dag;

    public DayOverview(DayOfWeek day) {
        this.day = day;
    }

    public void setDag(String dag) {
        this.dag = dag;
    }

    public String getDag() {
        return dag;
    }

    public List<PlayPerDay> getPlays() {
        return plays;
    }

    public void add(PlayPerDay playPerDay) {
        this.plays.add(playPerDay);
    }
}
