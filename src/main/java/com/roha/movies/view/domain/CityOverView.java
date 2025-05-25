package com.roha.movies.view.domain;

import com.roha.movies.components.MovieWithPlaysDiv;
import com.roha.movies.domain.Movie;
import com.roha.movies.domain.PlayDTO;
import com.roha.movies.fetcher.Initializer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.dom.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

@Tag("city-overview")
public class CityOverView extends HtmlContainer {
    public CityOverView() {
    }


    List <Movie> movies = new ArrayList<>();
    public CityOverView(Initializer initializer, String city) throws IOException {
        LinkedHashMap<String, Movie> movies = new LinkedHashMap<>();
        List<PlayDTO> playDTOS = initializer.loadPlays(city);
        for (int i = 0; i < playDTOS.size(); i++) {

            PlayDTO playDTO = playDTOS.get(i);
            Movie movie = movies.get(playDTO.movieDTO().movieId());
            if (movie == null){
                movie = playDTO.movieDTO().asMovie();
            }
            movie.plays().add(playDTO.withMovie(movie));
            movies.put(playDTO.movieDTO().movieId(), movie);
        }
        this.movies.addAll(movies.values());
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
