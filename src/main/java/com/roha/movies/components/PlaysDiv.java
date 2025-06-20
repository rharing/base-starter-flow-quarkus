package com.roha.movies.components;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.roha.movies.domain.Movie;
import com.roha.movies.domain.Play;
import com.roha.movies.view.CityView;
import com.roha.movies.view.domain.DayOverview;
import com.roha.movies.view.domain.PlayPerDay;
import com.roha.movies.view.domain.WeekOverView;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@Tag("plays-div")
public class PlaysDiv extends HtmlContainer {

    private final Map<String, List<Anchor>> cinemaLinks;
    private final CityView parent;

    public PlaysDiv(Movie movie, CityView parent) {
        this.parent = parent;
        VerticalLayout layout = new VerticalLayout();
        layout.add(new H3("Plays for " + movie.title()));
        this.add(layout);

        WeekOverView result = new WeekOverView();
        for (Play play : movie.plays()) {
            result.add(new PlayPerDay(play));
        }

        HorizontalLayout cinemas = new HorizontalLayout();
        cinemaLinks = new HashMap<>();

        for (String cinema : result.getCinemas()) {
            Checkbox checkbox = new Checkbox(cinema);
            cinemaLinks.put(cinema, new ArrayList<>());
            checkbox.setValue(preferredCinemas().contains(cinema));
            checkbox.addValueChangeListener(event -> {
//                ComponentUtil.fireEvent(this, new CinemaSelectedEvent(checkbox, event.getValue(), false));
                showDays(new CinemaSelectedEvent(checkbox, cinema, true));
            });
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
            // Textual link
            dayOverview.getPlays().forEach(playPerDay -> {
                Anchor link = new Anchor(playPerDay.ticket(), playPerDay.toString());
                link.setVisible(preferredCinemas().contains(playPerDay.cinema()));
                cinemaLinks.get(playPerDay.cinema()).add(link);
                link.setTarget("_blank");
                dayLayout.add(link);
            });
            days.add(day);
        }
        layout.add(days);
    }

    private List<String> preferredCinemas() {
        return Arrays.asList("Schuur", "FilmKoepel");
    }

    private void showDays(final CinemaSelectedEvent event) {
        cinemaLinks.get(event.getCinema()).forEach(anchor -> anchor.setVisible(event.getSelected()));
    }



    private Clock clock() {
            return Clock.systemDefaultZone();
    }
}
