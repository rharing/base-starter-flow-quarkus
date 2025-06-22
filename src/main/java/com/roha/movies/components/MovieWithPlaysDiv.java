package com.roha.movies.components;

import com.roha.movies.domain.Movie;
import com.roha.movies.domain.WithLogger;
import com.roha.movies.fetcher.Initializer;
import com.roha.movies.view.CityView;
import com.roha.movies.view.domain.MyMoviesAction;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.io.IOException;

public class MovieWithPlaysDiv extends Card implements WithLogger {
    private Movie movie;
    private Initializer initializer;

    public MovieWithPlaysDiv(Initializer initializer, Movie movie) {
        this(initializer, movie, null, null);
    }

    public MovieWithPlaysDiv(Initializer initializer, Movie movie, CityView parent, MyMoviesAction action) {
        super();
        this.movie = movie;
        this.initializer = initializer;
        addThemeVariants(
                CardVariant.LUMO_OUTLINED,
                CardVariant.LUMO_ELEVATED,
                CardVariant.LUMO_HORIZONTAL,
                CardVariant.LUMO_COVER_MEDIA
        );
        setSubtitle(new H3(movie.title() + " - " + movie.rating()));
        VerticalLayout movieCard = new VerticalLayout();
        Paragraph movieText = new Paragraph();
        movieText.setText("");
        movieCard.add(movieText);
        movieCard.add(new PlaysDiv(movie));
        Span moviePoster = new Span(locatePosterImage(movie, action));
        moviePoster.addClickListener(event -> {
            Movie movie1 = null;
            try {
                movie1 = initializer.loadMovie(movie.asDTO());
                movieText.setText(movie1.content());
            } catch (IOException ex) {
                logger().error("could not load movie content", ex);
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
        HorizontalLayout buttons = new HorizontalLayout(FlexComponent.Alignment.START, skipButton, seenButton, wantedButton, resetButton);
        this.addToFooter(buttons);
//        setSubtitle(buttons);
    }

    private Image locatePosterImage(Movie movie, MyMoviesAction action) {
        Image media =null;
        if (movie.imageHref() == null && movie.href() != null) {
            try {
                Movie updatedMyMovie = initializer.updateMyMovie(movie.asDTO(), action);
                media = new Image(updatedMyMovie.imageHref(), updatedMyMovie.title());

            } catch (IOException e) {
                logger().error("could not load movie content for the image {}", movie.toString(), e);
            }
        }
        else {
            media = new Image(movie.imageHref(), movie.title());

        }
        return media;
    }

    private Button createButton(String text, CityView parent, MyMoviesAction action) {
        Button button = new Button(text);
        button.addClickListener(event -> {
            ComponentUtil.fireEvent(parent, new ReloadMoviesEvent(this, movie, action));
            if (action == MyMoviesAction.SKIPPED) {
                this.setVisible(false);
            }
        });
        button.addClassName("button44");
        return button;
    }
}
