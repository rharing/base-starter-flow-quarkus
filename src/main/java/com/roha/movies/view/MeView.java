package com.roha.movies.view;

import java.util.Map;

import com.roha.movies.components.MovieWithPlaysDiv;
import com.roha.movies.domain.Movie;
import com.roha.movies.domain.MovieDTO;
import com.roha.movies.domain.MyMovies;
import com.roha.movies.domain.WithLogger;
import com.roha.movies.fetcher.Initializer;
import com.roha.movies.view.domain.MyMoviesAction;
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
        VerticalLayout movieView = new VerticalLayout();
        specificMovies(movieView, myMovies.getWanted(), MyMoviesAction.WANTED);

        Tab seen = new Tab("Seen");
        Tab wanted = new Tab("Wanted");

        Tabs tabs = new Tabs(wanted, seen, skipped);

        tabs.setSelectedTab(wanted);

        tabs.addSelectedChangeListener(event -> {
            if (event.getSelectedTab() == wanted) {
                movieView.removeAll();
                specificMovies(movieView, myMovies.getWanted(), MyMoviesAction.WANTED);
            } else if (event.getSelectedTab() == seen) {
                movieView.removeAll();
                specificMovies(movieView, myMovies.getSeen(), MyMoviesAction.SEEN);
            } else if (event.getSelectedTab() == skipped) {
                movieView.removeAll();
                specificMovies(movieView, myMovies.getSkipped(), MyMoviesAction.SKIPPED);
            }
        });
        add(tabs);
        add(movieView);
    }

    private void specificMovies(final VerticalLayout movieView, final Map<String, MovieDTO> wanted, final MyMoviesAction myMoviesAction) {
        wanted.forEach((key, value) -> {
            if (value != null) {
                Movie movie = value.asMovie();
                logger().info(myMoviesAction + "Movie " + movie.title());
                movieView.add(new MovieWithPlaysDiv(initializer, movie, myMoviesAction));
            }
        });
    }

}
