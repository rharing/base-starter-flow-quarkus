package com.roha.movies.view.domain;

import com.roha.movies.domain.Movie;
import com.roha.movies.fetcher.Initializer;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Tag("city-overview")
public class CityOverView extends HtmlContainer {
    public CityOverView() {
    }


    List <Movie> movies = new ArrayList<>();
    public CityOverView(Initializer initializer, String city) throws IOException {
        this.movies = initializer.loadPlaysByMovie(city);
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
