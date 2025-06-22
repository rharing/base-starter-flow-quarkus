package com.roha.movies;

import com.roha.movies.domain.*;
import com.roha.movies.fetcher.Initializer;
import com.roha.movies.fetcher.MoviesFetcher;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.List;

//@RestController
//@RequestMapping("/movie")
//@CrossOrigin
public class MovieResource {

    @Inject
    Initializer initializer;

    public MovieResource(Initializer initializer) {
        this.initializer = initializer;
    }

    //    @PostMapping
    public Movie movie(MovieDTO movieDTO) throws IOException {
        return initializer.loadMovie(movieDTO);
    }

    //    @PostMapping("/skip")
    public void skipMovie(MovieDTO4MyMovies movieDTO4MyMovies) throws IOException {
        MoviesFetcher moviesFetcher = initializer.getMoviesFetcher();
        moviesFetcher.addSkipped(movieDTO4MyMovies.asMovieDTO());
    }

    //    @PostMapping("/want")
    public void wantedMovie(MovieDTO4MyMovies movieDTO4MyMovies) throws IOException {
        MoviesFetcher moviesFetcher = initializer.getMoviesFetcher();
        moviesFetcher.addWanted(movieDTO4MyMovies.asMovieDTO());
    }

    //    @PostMapping("/seen")
    public void seenMovie(MovieDTO4MyMovies movieDTO4MyMovies) throws IOException {
        MoviesFetcher moviesFetcher = initializer.getMoviesFetcher();
        moviesFetcher.addSeen(movieDTO4MyMovies.asMovieDTO());
    }

    //    @PostMapping("/reset")
    public void resetMovie(MovieDTO4MyMovies movieDTO4MyMovies) throws IOException {
        MoviesFetcher moviesFetcher = initializer.getMoviesFetcher();
        moviesFetcher.reset(movieDTO4MyMovies.asMovieDTO());
    }

    //    @PostMapping("/when")
    public List<WhenPlayDTO> whenMovie(MovieDTO4MyMovies movieDTO4MyMovies) throws IOException {
        MoviesFetcher moviesFetcher = initializer.getMoviesFetcher();
        return moviesFetcher.when(movieDTO4MyMovies.asMovieDTO());
    }
}
