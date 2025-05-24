package com.roha.movies.components;

import com.roha.movies.domain.Movie;
import com.roha.movies.domain.MovieDTO;
import com.roha.movies.fetcher.Initializer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.io.IOException;

public class MovieWithPlaysDiv extends Card {
    private Movie movie;
    private Initializer initializer;

    public MovieWithPlaysDiv(Initializer initializer, Movie movie) {
        super();
        this.movie = movie;
        this.initializer = initializer;
        setTitle(movie.title() + " - " + movie.rating());
        Paragraph movieContent = new Paragraph();
        movieContent.setText("");
        Image media = new Image(movie.imageHref(), movie.title());
        Span span = new Span(media);
        span.addClickListener(event -> {
            Movie movie1 = null;
            try {
                movie1 = initializer.loadMovie(movie.asDTO());
                movieContent.setText(movie1.content());
            } catch (IOException ex) {
                movieContent.setText("Could not load content");
            }
            ;
        });
        this.setMedia(span);
        this.add(movieContent);
        Button skipButton = new Button("Skip");
        skipButton.addClickListener(event -> {
            try {
                initializer.getMoviesFetcher().addSkipped(movie.asDTO());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        Button seenButton = new Button("Seen");
        skipButton.addClickListener(event -> {
            try {
                initializer.getMoviesFetcher().addSeen(movie.asDTO());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        Button wantedButton = new Button("Yessss");
        skipButton.addClickListener(event -> {
            try {
                initializer.getMoviesFetcher().addSeen(movie.asDTO());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        Button resetButton = new Button("Reset");
        skipButton.addClickListener(event -> {
            try {
                initializer.getMoviesFetcher().reset(movie.asDTO());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        HorizontalLayout buttons = new HorizontalLayout(FlexComponent.Alignment.START,skipButton, seenButton, wantedButton, resetButton);
        setSubtitle(buttons);
    }
}
