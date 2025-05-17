package com.roha.movies.fetcher;

import com.roha.movies.domain.MyMovies;

import java.io.IOException;

public class LocalMyMoviesRepositoryForTest implements MyMoviesRepository {
    MyMovies myMovies = new MyMovies();

    public LocalMyMoviesRepositoryForTest() {

    }

    @Override
    public MyMovies load() {
        return myMovies;
    }

    @Override
    public void save(MyMovies myMovies) throws IOException {
        myMovies = myMovies;
    }

    @Override
    public void clean() {
        myMovies = new MyMovies();
    }
}
