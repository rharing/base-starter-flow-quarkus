package com.roha.movies.domain;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class MyMovies {
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
        if (movieDTO.getMovieId() != null) {

            boolean exists = wanted.containsKey(movieDTO.getMovieId());
            if (!exists) {
                wanted.put(movieDTO.getMovieId(), movieDTO);
                return true;
            }
        }
        return false;
    }

    public boolean addSeen(MovieDTO movieDTO) {
        if (movieDTO.getMovieId() != null) {

            boolean exists = seen.containsKey(movieDTO.getMovieId());
            if (!exists) {
                seen.put(movieDTO.getMovieId(), movieDTO);
                return true;
            }
            if (wanted.containsKey(movieDTO.getMovieId())) {
                wanted.remove(movieDTO.getMovieId());
                return true;
            }
        }
        return false;
    }

    public boolean addSkipped(MovieDTO movieDTO) {
        if (movieDTO.getMovieId() != null) {
            boolean exists = skipped.containsKey(movieDTO.getMovieId());
            if (!exists) {
                skipped.put(movieDTO.getMovieId(), movieDTO);
                return true;
            }
        }
        return false;
    }
}
