package com.roha.movies.view;

import com.roha.movies.components.MovieWithPlaysDiv;
import com.roha.movies.domain.Movie;
import com.roha.movies.domain.MyMovies;
import com.roha.movies.domain.WithLogger;
import com.roha.movies.fetcher.Initializer;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;

@Route("me")
public class MeView extends VerticalLayout implements WithLogger {
    private final Initializer initializer;

    public MeView(Initializer initializer) {
        this.initializer = initializer;
        MyMovies myMovies = initializer.getMyMovies();
        Tab skipped = new Tab("Skipped");
        VerticalLayout skippedView = new VerticalLayout();
        myMovies.getWanted().forEach((key, value) -> {
            Movie movie = value.asMovie();
            logger().info("Movie " + movie.title());
            MovieWithPlaysDiv movieWithPlaysDiv = new MovieWithPlaysDiv(initializer, movie);
            skippedView.add(movieWithPlaysDiv);
        });

        skipped.add(skippedView);
        Tab seen = new Tab("Seen");
        Tab wanted = new Tab("Wanted");
        Tabs tabs = new Tabs(wanted, seen, skipped);
        tabs.setSelectedTab(wanted);

        tabs.addSelectedChangeListener(event -> {
            if (event.getSelectedTab() == wanted) {
            } else if (event.getSelectedTab() == seen) {
                VerticalLayout movies = new VerticalLayout();
                myMovies.getSeen().forEach((key, value) -> {
                    MovieWithPlaysDiv movieWithPlaysDiv = new MovieWithPlaysDiv(initializer, value.asMovie());
                    movies.add(movieWithPlaysDiv);
                });
                add(movies);
            } else if (event.getSelectedTab() == skipped) {
                VerticalLayout movies = new VerticalLayout();
                myMovies.getSkipped().forEach((key, value) -> {
                    MovieWithPlaysDiv movieWithPlaysDiv = new MovieWithPlaysDiv(initializer, value.asMovie());
                    movies.add(movieWithPlaysDiv);
                });
                add(movies);
            }
        });
        add(tabs);
    }

}
