package com.roha.movies.domain;


import java.util.HashMap;
import java.util.Map;

public class MyMovies {
    public MyMovies() {
    }

    public MyMovies(Map<String, MovieDTO> wanted, Map<String, MovieDTO> seen, Map<String, MovieDTO> skipped) {
        this.wanted = wanted;
        this.seen = seen;
        this.skipped = skipped;
    }

    private Map<String, MovieDTO> wanted = new HashMap<>();
    private Map<String, MovieDTO> seen = new HashMap<>();
    private Map<String, MovieDTO> skipped = new HashMap<>();

    public Map<String, MovieDTO> getWanted() {
        return wanted;
    }

    public Map<String, MovieDTO> getSeen() {
        return seen;
    }

    public Map<String, MovieDTO> getSkipped() {
        return skipped;
    }

    public boolean addWanted(MovieDTO movieDTO) {
        if (movieDTO.movieId() != null) {

            boolean exists = wanted.containsKey(movieDTO.movieId());
            if (!exists) {
                wanted.put(movieDTO.movieId(), movieDTO);
                return true;
            }
        }
        return false;
    }

    public boolean addSeen(MovieDTO movieDTO) {
        if (movieDTO.movieId() != null) {

            boolean exists = seen.containsKey(movieDTO.movieId());
            if (!exists) {
                seen.put(movieDTO.movieId(), movieDTO);
                return true;
            }
            if (wanted.containsKey(movieDTO.movieId())) {
                wanted.remove(movieDTO.movieId());
                return true;
            }
        }
        return false;
    }

    public boolean addSkipped(MovieDTO movieDTO) {
        if (movieDTO.movieId() != null) {
            boolean exists = skipped.containsKey(movieDTO.movieId());
            if (!exists) {
                skipped.put(movieDTO.movieId(), movieDTO);
                return true;
            }
        }
        return false;
    }
}
