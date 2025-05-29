package com.roha.movies.components;

import com.roha.movies.domain.Movie;
import com.roha.movies.fetcher.Initializer;
import com.roha.movies.view.CityView;
import com.roha.movies.view.domain.MyMoviesAction;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
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

    public MovieWithPlaysDiv(Initializer initializer, Movie movie, CityView parent) {
        super();
        this.movie = movie;
        this.initializer = initializer;
        setTitle(movie.title() + " - " + movie.rating());
        VerticalLayout movieCard = new VerticalLayout();
        Paragraph movieText = new Paragraph();
        movieText.setText("");
        movieCard.add(movieText);
        movieCard.add(new PlaysDiv( movie, parent));
        Image media = new Image(movie.imageHref(), movie.title());
        Span moviePoster = new Span(media);
        moviePoster.addClickListener(event -> {
            Movie movie1 = null;
            try {
                movie1 = initializer.loadMovie(movie.asDTO());
                movieText.setText(movie1.content());
            } catch (IOException ex) {
                movieText.setText("Could not load content");
            }
            ;
        });
        this.setMedia(moviePoster);
        this.add(movieCard);
        Button skipButton = createButton("Skip", parent, MyMoviesAction.SKIPPED);
        Button seenButton = createButton("Seen", parent, MyMoviesAction.SEEN);
        Button wantedButton = createButton("Wanted", parent, MyMoviesAction.WANTED);
        Button resetButton = createButton("Reset", parent, MyMoviesAction.RESET);
        HorizontalLayout buttons = new HorizontalLayout(FlexComponent.Alignment.START,skipButton, seenButton, wantedButton, resetButton);
        setSubtitle(buttons);
    }

    private Button createButton(String text, CityView parent, MyMoviesAction action) {
        Button button = new Button(text);
        button.addClickListener(event -> {
            ComponentUtil.fireEvent(parent, new ReloadMoviesEvent(this, movie, action));
            this.setVisible(false);
        });
        button.addClassName("button44");
        return button;
    }
}
