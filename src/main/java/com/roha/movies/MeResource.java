package com.roha.movies;

import com.roha.movies.domain.MovieDTO;
import com.roha.movies.domain.MovieDTO4MyMovies;
import com.roha.movies.domain.MyMovies;
import com.roha.movies.fetcher.Initializer;
import com.roha.movies.fetcher.MoviesFetcher;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

//@RestController
//@RequestMapping("/me")
//@CrossOrigin
public class MeResource {

    private Initializer initializer;

    public MeResource(Initializer initializer) {
        this.initializer = initializer;
    }

    //    @GetMapping("")
    public MyMovies me() throws IOException {
        MyMovies myMovies = initializer.getMyMoviesRepository().load();
//        boolean updateWanted = checkHrefs(myMovies.getWanted().values());
//        boolean updateSeen = checkHrefs(myMovies.getSeen().values());
        final List<MovieDTO> oldSkipped = removeOldSkipped((LinkedHashMap<String, MovieDTO>) myMovies.getSkipped());
        boolean updateSkipped = (oldSkipped.size() != myMovies.getSkipped().size());
//        boolean updateSkipped = true;
//        myMovies.getSkipped().clear();
//        if (updateWanted || updateSeen || updateSkipped) {
//            initializer.getMyMoviesRepository().save(myMovies);
//        }
        return myMovies;
    }

    //    @GetMapping("/reset/{what}")
    public void reset(String what) throws IOException {
        MyMovies myMovies = initializer.getMyMoviesRepository().load();
        if (what.equals("skipped")) {
            myMovies.getSkipped().clear();
            initializer.getMyMoviesRepository().save(myMovies);
        }
    }


    private List<MovieDTO> removeOldSkipped(final LinkedHashMap<String, MovieDTO> skipped) {
        return skipped.values().stream().filter(movieDTO -> movieDTO.createdAt() != null).toList();

    }

/*
    private boolean checkHrefs(Collection<MovieDTO> movies) {
        boolean update = false;
        for (MovieDTO movieDTO : movies) {
            if (movieDTO.href() != null && !movieDTO.href().startsWith("http")) {
                if (movieDTO.rating().startsWith("http")) {
                    movieDTO movieDTO = movieDTO.withHref(movieDTO.rating());
                    movies.
                    update = true;
                }
            }
        }
        return update;
    }
*/

    //    @PostMapping("/skip")
//    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void skipMovie(MovieDTO4MyMovies movieDTO4MyMoviesieDTO) throws IOException {
        MoviesFetcher moviesFetcher = initializer.getMoviesFetcher();
        moviesFetcher.addSkipped(movieDTO4MyMoviesieDTO.asMovieDTO());

    }

    //    @PostMapping("/want")
//    @ResponseStatus(value = NO_CONTENT)
    public void wantedMovie(MovieDTO4MyMovies movieDTO4MyMoviesieDTO) throws IOException {
        MoviesFetcher moviesFetcher = initializer.getMoviesFetcher();
        moviesFetcher.addWanted(movieDTO4MyMoviesieDTO.asMovieDTO());
    }

    //    @PostMapping("/seen")
//    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void seenMovie(MovieDTO4MyMovies movieDTO4MyMoviesieDTO) throws IOException {
        MoviesFetcher moviesFetcher = initializer.getMoviesFetcher();
        moviesFetcher.addSeen(movieDTO4MyMoviesieDTO.asMovieDTO());
    }
}
