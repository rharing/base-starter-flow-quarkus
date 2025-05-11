package com.roha.movies.fetcher;

import com.roha.movies.domain.MyMovies;

import java.io.IOException;

public interface MyMoviesRepository {

    MyMovies load();
    void save(MyMovies movie) throws IOException;

    void clean();
}
