package com.roha.movies.fetcher;

import com.roha.movies.domain.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
/**
 * uses a local json as mymovies repository and an local overview_haarlem  page to locate the cities and the movies
 */
@Slf4j
public class MoviesFetcher {

    private final MoviesDocumentParser moviesDocumentParser;
    private Clock clock = Clock.systemDefaultZone();
    private final MyMoviesRepository myMoviesRepository;

    public MoviesFetcher(DocumentLoader documentLoader) {
        this(new MoviesDocumentParser(documentLoader), new LocalMyMoviesRepository("my_movies.json"));
    }

    public MoviesFetcher(MoviesDocumentParser moviesDocumentParser, MyMoviesRepository myMoviesRepository) {
        this(moviesDocumentParser, myMoviesRepository, Clock.systemDefaultZone());
    }

    public MoviesFetcher(MoviesDocumentParser moviesDocumentParser, MyMoviesRepository myMoviesRepository, Clock clock) {
        this.moviesDocumentParser = moviesDocumentParser;
        this.clock = clock;
        this.myMoviesRepository = myMoviesRepository;
    }

    public List<City> loadCities() throws IOException {
        return moviesDocumentParser.loadCities();
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    /*
        or as ghat gpt does it
         */
    public List<Play> getFilteredPlays(String city, Clock clock) throws IOException {
        List<Play> allplays = moviesDocumentParser.locateMoviesWithPlays(city).stream()
                .flatMap(movie -> movie.plays().stream()).toList();
        List<Play> plays = allplays.stream()
                .filter(play -> play.isOnTime(LocalDateTime.now(clock)))
                .collect(Collectors.toList());
        return plays;
    }

    public List<PlayDTO> loadPlays(String city) throws IOException {
        List<PlayDTO> result = new ArrayList<>();
        MyMovies myMovies = myMoviesRepository.load();
        for (Play play : getFilteredPlays(city, clock)) {
            if (myMovies.getSeen().containsKey(play.movie().id()) || myMovies.getSkipped().containsKey(play.movie().id())) {
                //skip it
//                System.out.println("skipping = " + play.movie().title());
            } else {
                result.add(play.asDTO());
            }
        }

        if(!result.isEmpty()) {
            Collections.sort(result, Comparator.comparing(PlayDTO::start).thenComparing(o -> o.movieDTO().getMovieId().toLowerCase()).thenComparing((o1, o2) -> o1.cinema().compareTo(o2.cinema())));
        }
        return result;
    }

    public void seenMovie(String id) throws IOException {
        MyMovies myMovies = myMoviesRepository.load();
        myMovies.getSeen().put(id,empty(id));
        myMoviesRepository.save(myMovies);
    }

    private MovieDTO empty(String id) {
        return new MovieDTO(id, id, id,id, id, id, id, null,null);
    }

    public void wantedMovie(String id) throws IOException {
        MyMovies myMovies = myMoviesRepository.load();
        myMovies.getSeen().put(id,empty(id));
        myMoviesRepository.save(myMovies);
    }

    public void skippedMovie(String id) throws IOException {
        MyMovies myMovies = myMoviesRepository.load();
        myMovies.getSkipped().put(id, empty(id));
        myMoviesRepository.save(myMovies);
    }

    public void resetMovie(String id) throws IOException {
        MyMovies myMovies = myMoviesRepository.load();
        myMovies.getSeen().remove(id);
        myMovies.getSkipped().remove(id);
        myMovies.getWanted().remove(id);
        myMoviesRepository.save(myMovies);
    }

    public Movie loadMovie() throws IOException {
        return moviesDocumentParser.loadMovie(null);
    }

    public Movie loadMovie(MovieDTO movieDTO) throws IOException {
        if (movieDTO != null && movieDTO.getHref() != null) {

            return moviesDocumentParser.loadMovie(movieDTO.getHref());
        }
        return moviesDocumentParser.loadMovie(null);
    }

    public void addSkipped(MovieDTO movieDTO) throws IOException {
        MyMovies myMovies = myMoviesRepository.load();
        boolean updateMyMovies = myMovies.addSkipped(movieDTO);
        if (updateMyMovies) {
            myMoviesRepository.save(myMovies);
        }
    }

    public void addWanted(MovieDTO movieDTO) throws IOException {
        MyMovies myMovies = myMoviesRepository.load();
        boolean updateMyMovies = myMovies.addWanted(movieDTO);
        if (updateMyMovies) {
            myMoviesRepository.save(myMovies);
        }
    }

    public void addSeen(MovieDTO movieDTO) throws IOException {
        MyMovies myMovies = myMoviesRepository.load();
        boolean updateMyMovies = myMovies.addSeen(movieDTO);
        if(myMovies.getWanted().containsKey(movieDTO.getMovieId())){
            myMovies.getWanted().remove(movieDTO.getMovieId());
            updateMyMovies = true;
        }
        if (updateMyMovies) {
            myMoviesRepository.save(myMovies);
        }
    }

    public void reset(MovieDTO movieDTO) throws IOException {
        MyMovies myMovies = myMoviesRepository.load();
        boolean update = false;
        if (myMovies.getSeen().containsKey(movieDTO.getMovieId())) {
            myMovies.getSeen().remove(movieDTO.getMovieId());
            update = true;
        }
        if (myMovies.getWanted().containsKey(movieDTO.getMovieId())) {
            myMovies.getWanted().remove(movieDTO.getMovieId());
            update = true;
        }
        if (myMovies.getSkipped().containsKey(movieDTO.getMovieId())) {
            myMovies.getSkipped().remove(movieDTO.getMovieId());
            update = true;
        }
        if (update) {
            myMoviesRepository.save(myMovies);
        }
    }

    public List<WhenPlayDTO> when(MovieDTO movieDTO) throws IOException {
        if (movieDTO != null && movieDTO.getId() != null) {

            List<WhenPlayDTO> whenPlayDTOS = moviesDocumentParser.whenMovie(movieDTO.getId());
            whenPlayDTOS.sort(new Comparator<WhenPlayDTO>() {
                @Override
                public int compare(WhenPlayDTO o1, WhenPlayDTO o2) {
                    int compare =o1.city().compareTo(o2.city());
                    if (compare == 0) {
                        compare = o2.start().compareTo(o1.start());
                    }
                    return compare;
                }
            });
            return whenPlayDTOS;
        }
        return null;
    }
}
