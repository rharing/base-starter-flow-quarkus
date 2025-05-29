package com.roha.movies.components;

import com.roha.movies.domain.Movie;
import com.roha.movies.service.PlayGroupingService;
import com.roha.movies.view.CityView;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@Tag("plays-div")
public class PlaysDiv extends HtmlContainer {
    private final PlayGroupingService playGroupingService = new PlayGroupingService();
    
    public PlaysDiv(Movie movie, CityView parent) {
        VerticalLayout layout = new VerticalLayout();
        layout.add(new H3("Plays for " + movie.title()));
        this.add(layout);

        var result = playGroupingService.groupPlaysByDay(movie);

        var playsByDay = result.playsByDay();
        
        // TODO: Use the grouped plays and cinemas as needed
    }
}
