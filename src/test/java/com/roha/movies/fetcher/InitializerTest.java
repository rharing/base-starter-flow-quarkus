package com.roha.movies.fetcher;

import com.roha.movies.domain.Movie;
import com.roha.movies.domain.Play;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.roha.movies.TestFixtures.createMoviesFetcher;
import static org.assertj.core.api.Assertions.assertThat;

class InitializerTest {

    @Test
    public void locateMoviesWithPlays() throws IOException {
        MoviesFetcher moviesFetcher = createMoviesFetcher("31-mei.html", null);
        Initializer initializer = Initializer.InitializerBuilder.InitializerForTesting().build();
        initializer.setMoviesFetcher(moviesFetcher);
        List<Movie> movies = initializer.loadPlaysByMovie("haarlem");
        List<Movie> mission = movies.stream().filter(movie -> movie.title().contains("Mission")).toList();
        assertThat(mission).hasSize(1);
        assertThat(mission.get(0).plays()).hasSize(57);
        Set<String> cinemas = mission.get(0).plays().stream().map(Play::cinema).collect(Collectors.toSet());
        assertThat(cinemas).hasSize(3);
    }
}