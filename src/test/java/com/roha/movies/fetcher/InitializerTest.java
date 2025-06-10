package com.roha.movies.fetcher;

import com.roha.movies.domain.Movie;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.roha.movies.TestFixtures.createMoviesFetcher;
import static org.assertj.core.api.Assertions.assertThat;

class InitializerTest {

    @Test
    public void locateMoviesWithPlays() throws IOException {
        MoviesFetcher moviesFetcher = createMoviesFetcher("31-mei.html", null);
        Initializer initializer = new Initializer();
        initializer.setMoviesFetcher(moviesFetcher);
        List<Movie> movies = initializer.loadPlaysByMovie("haarlem");
        List<Movie> mission = movies.stream().filter(movie -> movie.title().contains("Mission")).toList();
        assertThat(mission).hasSize(1);
    }
}