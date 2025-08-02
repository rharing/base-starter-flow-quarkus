package com.roha.movies.components;

import com.google.common.base.Strings;
import com.roha.movies.domain.Movie;
import com.roha.movies.fetcher.Initializer;
import com.roha.movies.domain.WithLogger;
import com.roha.movies.view.domain.MyMoviesAction;
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
    public static final String LOADING = "Loading...";
    private Movie movie;
    private final Initializer initializer;


    public MovieWithPlaysDiv(Initializer initializer, Movie movie) {
        this(initializer, movie, null);
    }

    public MovieWithPlaysDiv(Initializer initializer, Movie movie, MyMoviesAction action) {
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
        Span moviePoster = new Span(locatePosterImage(movie));
        moviePoster.addClickListener(event -> {
            try {
                //@todo wrap movie in a view that keeps track of thecontent so we dont fetch it again if not needed
//                movieText.setText(LOADING);
                movieText.setVisible(!movieText.isVisible());
                if (Strings.isNullOrEmpty(movie.content())) {
                    this.movie = initializer.loadMovie(movie.asDTO());
                    String content = this.movie.content();
                    if (Strings.isNullOrEmpty(content)) {
                        content = "niet gevonden";
                    }
                    movieText.setText(content);
                    movieText.setVisible(true);
                } else {
                    movieText.setVisible(!movieText.isVisible());
                }
            } catch (IOException ex) {
                logger().error("could not load movie content", ex);
                movieText.setText("Could not load content");
            }

        });
        this.setMedia(moviePoster);
        this.add(movieCard);

        Button skipButton = createButton("Skip", MyMoviesAction.SKIPPED);
        Button seenButton = createButton("Seen", MyMoviesAction.SEEN);
        Button wantedButton = createButton("Wanted", MyMoviesAction.WANTED);
        Button resetButton = createButton("Reset", MyMoviesAction.RESET);
        HorizontalLayout buttons = new HorizontalLayout(FlexComponent.Alignment.START, skipButton, seenButton, wantedButton, resetButton);
        if (action != null) {
            switch (action) {
                case SKIPPED:
                    skipButton.setVisible(false);
                    break;
                case SEEN:
                    seenButton.setVisible(false);
                    break;
                case WANTED:
                    wantedButton.setVisible(false);
                    break;
                case RESET:
                    resetButton.click();
                    break;
            }

        }
        this.addToFooter(buttons);
    }

    private Image locatePosterImage(Movie movie) {
        Image media = null;
        if (movie.imageHref() == null && movie.href() != null) {
            try {
                Movie updatedMyMovie = initializer.loadMovie(movie.asDTO());
                media = new Image(updatedMyMovie.imageHref(), updatedMyMovie.title());

            } catch (IOException e) {
                logger().error("could not load movie content for the image {}", movie, e);
            }
        } else {
            if (movie.imageHref() != null) {
                media = new Image(movie.imageHref(), movie.title());
            } else {
                logger().info("movie image is null {}", movie.title());
            }
        }
        if (media == null) {
            logger().warn("movie image is null {}", movie.title());

        }
        return media;
    }

    private Button createButton(String text, MyMoviesAction action) {
        Button button = new Button(text);
        button.addClickListener(event -> {
            try {
                initializer.updateMyMovie(movie.asDTO(), action);
                if (action == MyMoviesAction.SKIPPED || action == MyMoviesAction.SEEN) {
                    this.setVisible(false);
                }
            } catch (IOException e) {
                logger().error("could not update mymovies for movie {}", movie.asDTO(), e);
            }
        });
        button.addClassName("button44");
        return button;
    }
}
