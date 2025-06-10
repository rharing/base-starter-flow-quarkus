package com.roha.movies.components;

import com.roha.movies.domain.Movie;
import com.roha.movies.domain.Play;
import com.roha.movies.view.CityView;
import com.roha.movies.view.domain.DayOverview;
import com.roha.movies.view.domain.PlayPerDay;
import com.roha.movies.view.domain.WeekOverView;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Tag("plays-div")
public class PlaysDiv extends HtmlContainer {

    public PlaysDiv(Movie movie, CityView parent) {
        VerticalLayout layout = new VerticalLayout();
        layout.add(new H3("Plays for " + movie.title()));
        this.add(layout);

        WeekOverView result = new WeekOverView();
        for (Play play : movie.plays()) {
            result.add(new PlayPerDay(play));
        }

        HorizontalLayout cinemas = new HorizontalLayout();
        for (String cinema : result.getCinemas()) {
            Checkbox checkbox = new Checkbox(cinema);
            cinemas.add(checkbox);
        }
        layout.add(cinemas);

        HorizontalLayout days = new HorizontalLayout();
        List<DayOverview> overview = result.getOverview(LocalDate.now(clock()));
        for (DayOverview dayOverview : overview) {
            HorizontalLayout day = new HorizontalLayout();
            VerticalLayout dayLayout = new VerticalLayout();

            dayLayout.add(new H3(dayOverview.getDag()));
            day.add(dayLayout);
            days.add(day);
        }
        layout.add(days);
    }

    private Clock clock() {
            return Clock.systemDefaultZone();
    }
}
