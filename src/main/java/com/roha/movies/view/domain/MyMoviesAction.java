package com.roha.movies.view.domain;

import com.roha.movies.domain.MovieDTO;
import com.roha.movies.domain.MyMovies;

public enum MyMoviesAction {
    SKIPPED {
        @Override
        public boolean handle(MyMovies myMovies, MovieDTO dto) {
            return myMovies.addSkipped(dto);
        }
    }, WANTED {
        @Override
        public boolean handle(MyMovies myMovies, MovieDTO dto) {
            return myMovies.addWanted(dto);
        }
    }, SEEN {
        @Override
        public boolean handle(MyMovies myMovies, MovieDTO dto) {
            return myMovies.addSeen(dto);
        }
    }, RESET {
        @Override
        public boolean handle(MyMovies myMovies, MovieDTO dto) {
            myMovies.getWanted().remove(dto.movieId());
            myMovies.getSeen().remove(dto.movieId());
            myMovies.getSkipped().remove(dto.movieId());
            return true;
        }
    };

    public abstract boolean handle(MyMovies myMovies, MovieDTO dto);
}
