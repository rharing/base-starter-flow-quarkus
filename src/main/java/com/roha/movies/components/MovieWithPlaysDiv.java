package com.roha.movies.components;

import com.roha.movies.domain.Movie;
import com.roha.movies.domain.MovieDTO;
import com.roha.movies.fetcher.Initializer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.io.IOException;

public class MovieWithPlaysDiv extends Div {
    private Movie movie;
    private Initializer initializer;
    public MovieWithPlaysDiv(Initializer initializer, Movie movie) {
        super();
        this.movie = movie;
        this.initializer = initializer;
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        VerticalLayout movieOverview = new VerticalLayout();
        movieOverview.add(new H3(movie.title()+" ("+movie.rating()+")"));
        movieOverview.add(new Image(movie.imageHref(), "poster"));
        horizontalLayout.add(movieOverview);
        VerticalLayout load = new VerticalLayout();
        Button content1 = new Button("Load");
        Div movieContent = new Div();
        movieContent.setVisible(false);
        content1.addClickListener(e -> {
                    Movie movie1 = null;
                    try {
                        movie1 = initializer.loadMovie(movie.asDTO());
                        movieContent.setText(movie1.content());
                    } catch (IOException ex) {
                        movieContent.setText("Could not load content");
                    }
                    movieContent.setVisible(true);
                });
        load.add(content1);
        load.add(movieContent);
        horizontalLayout.add(load);
        add(horizontalLayout);
    }

}
