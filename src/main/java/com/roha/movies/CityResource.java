package com.roha.movies;

import com.roha.movies.domain.City;
import com.roha.movies.domain.PlayDTO;
import com.roha.movies.fetcher.Initializer;
import com.roha.movies.fetcher.MoviesFetcher;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.List;

//@RestController
//@RequestMapping("/cities")

public class CityResource {
    @Inject
    Initializer initializer;

    public CityResource(Initializer initializer) {
        this.initializer = initializer;
    }

//    @GetMapping("")
    public List<City> cities() throws IOException {
        MoviesFetcher moviesFetcher = initializer.getMoviesFetcher();
        return  moviesFetcher.loadCities();
    }

//    @GetMapping("/{name}")
    public List<PlayDTO> LoadCity(String name) throws IOException {
        List<PlayDTO> playDTOS = initializer.loadPlays(name);
        return playDTOS;
    }
}
