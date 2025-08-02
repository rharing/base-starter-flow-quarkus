package com.roha.movies.service;

import com.roha.movies.domain.MyMovies;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;

@ApplicationScoped
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
