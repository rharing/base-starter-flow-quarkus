package com.roha.movies.components;

import com.roha.movies.domain.Movie;
import com.roha.movies.view.domain.MyMoviesAction;
import com.vaadin.flow.component.ComponentEvent;

public class ReloadMoviesEvent extends ComponentEvent<MovieWithPlaysDiv> {
    MyMoviesAction myMoviesAction;
    Movie movie;
    public ReloadMoviesEvent(MovieWithPlaysDiv movieWithPlaysDiv, Movie movie,MyMoviesAction myMoviesAction) {
        super(movieWithPlaysDiv, true);
        this.myMoviesAction = myMoviesAction;
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }

    public MyMoviesAction getMyMoviesAction() {
        return myMoviesAction;
    }
}
