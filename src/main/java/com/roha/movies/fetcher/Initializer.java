package com.roha.movies.fetcher;

import com.google.common.base.Strings;
import com.roha.movies.domain.Movie;
import com.roha.movies.domain.MovieDTO;
import com.roha.movies.domain.MyMovies;
import com.roha.movies.domain.PlayDTO;
import com.roha.movies.service.MailService;
import com.roha.movies.view.domain.MyMoviesAction;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class Initializer {

    @ConfigProperty(name = "useLive", defaultValue = "true")
    Boolean useLive;
    @ConfigProperty(name = "fakeMovieContent", defaultValue = "true")
    Boolean fakeMovieContent;
    @ConfigProperty(name = "useAws", defaultValue = "false")
    Boolean useAws;
    @ConfigProperty(name = "s3_bucket")
    String bucket_name;
    @ConfigProperty(name = "s3_region")
    String region;
    private MoviesFetcher moviesFetcher;
    private MyMoviesRepository myMoviesRepository;


    private BaseDataLoader baseDataLoader = new BaseDataLoader();
    public static final Clock LONGTIMEAGO = Clock.fixed(
            Instant.parse("2018-04-11T20:34:58Z"),
            ZoneOffset.UTC);

    //    @EventListener(ApplicationReadyEvent.class)
    public void init() throws IOException {
        DocumentLoader documentLoader;
        if (useLive) {
            documentLoader = new ExternalDocumentLoader("https://www.filmladder.nl/");
        } else {
            Optional<String> optionalUrl = baseDataLoader.getExternalUrl("overview_haarlem.html");
            if (optionalUrl.isPresent()) {
                documentLoader = new DocumentLoaderFromFile(optionalUrl.get());
            } else {
                // wont happen
                throw new RuntimeException("failed to load overview_haarlem.html");
            }

        }
        if (useAws) {
            myMoviesRepository = new AwsMyMoviesRepository(bucket_name, region);
        } else {
            Optional<String> externalUrl = baseDataLoader.getExternalUrl("my_movies.json");
            myMoviesRepository = new LocalMyMoviesRepository(externalUrl.get());
        }

        moviesFetcher = new MoviesFetcher(new MoviesDocumentParser(documentLoader), myMoviesRepository);

        if (!useLive) {
            moviesFetcher = new MoviesFetcher(new MoviesDocumentParser(documentLoader), new LocalMyMoviesRepositoryForTest());
            moviesFetcher.setClock(LONGTIMEAGO);
        }
    }

    public void setMoviesFetcher(MoviesFetcher moviesFetcher) {
        this.moviesFetcher = moviesFetcher;
    }

    public MoviesFetcher getMoviesFetcher() {
        return moviesFetcher;
    }

    public MyMoviesRepository getMyMoviesRepository() {
        return myMoviesRepository;
    }

    public List<PlayDTO> loadPlays(String name) throws IOException {
        List<PlayDTO> playDTOS = moviesFetcher.loadPlays(name);
        if (this.useLive) {
            return playDTOS;
        } else {
            return playDTOS;
        }
    }

    public Movie updateMyMovie(MovieDTO movieDTO, MyMoviesAction action) throws IOException {
        Movie movie = loadMovie(movieDTO);
        MyMovies myMovies = myMoviesRepository.load();
        if(action != null) {
            action.handle(myMovies, movie.asDTO());
        }
        myMoviesRepository.save(myMovies);
        return movie;
    }

    public Movie loadMovie(final MovieDTO dto, final MailService mailService) throws IOException {
        final Movie movie = loadMovie(dto);
        if(Strings.isNullOrEmpty(movie.content())) {
            mailService.sendNoContentMovie(movie);
        }
        return movie;
    }

    public Movie loadMovie(MovieDTO movieDTO) throws IOException {
        if (fakeMovieContent) {
            String movieId = movieDTO.movieId();
            return new Movie(movieId, movieId, movieDTO.href(), "unknown", "bogus content for " + movieId, "", 42, new ArrayList<>(), "");
        } else {
            return getMoviesFetcher().loadMovie(movieDTO);
        }
    }

    /**
     * Loads plays by movie for a specified city, organizing them into a map of movies.
     * This method fetches the plays for a given city and groups them by their respective movies.
     * If a movie is not already in the map, it is created from its DTO and added to the map.
     * Plays for each movie are also added to the corresponding movie's play list.
     *
     * @param city the name of the city for which plays are to be loaded
     * @return a list of PlayDTO objects representing the plays for the specified city
     * @throws IOException if an I/O error occurs during the loading of plays
     */
    public List<Movie> loadPlaysByMovie(String city) throws IOException {
        List<PlayDTO> playDTOS = this.loadPlays(city);
        LinkedHashMap<String, Movie> movies = new LinkedHashMap<>();
        for (int i = 0; i < playDTOS.size(); i++) {

            PlayDTO playDTO = playDTOS.get(i);
            Movie movie = movies.get(playDTO.movieDTO().movieId());
            if (movie == null) {
                movie = playDTO.movieDTO().asMovie();
            }
            movie.plays().add(playDTO.withMovie(movie));
            movies.put(playDTO.movieDTO().movieId(), movie);
        }

        return new ArrayList<>(movies.values());
    }

    public MyMovies getMyMovies() {
        return myMoviesRepository.load();
    }

    public static final class InitializerBuilder {
        private Boolean useLive;
        private Boolean fakeMovieContent;
        private Boolean useAws;

        private InitializerBuilder() {
        }

        public static InitializerBuilder anInitializer() {
            return new InitializerBuilder();
        }

        public static InitializerBuilder InitializerForTesting() {
            return new InitializerBuilder().withUseLive(false).withUseAws(false).withFakeMovieContent(true);
        }

        public InitializerBuilder withUseLive(Boolean useLive) {
            this.useLive = useLive;
            return this;
        }

        public InitializerBuilder withFakeMovieContent(Boolean fakeMovieContent) {
            this.fakeMovieContent = fakeMovieContent;
            return this;
        }

        public InitializerBuilder withUseAws(Boolean useAws) {
            this.useAws = useAws;
            return this;
        }

        public Initializer build() {
            Initializer initializer = new Initializer();
            initializer.fakeMovieContent = this.fakeMovieContent;
            initializer.useAws = this.useAws;
            initializer.useLive = this.useLive;
            return initializer;
        }
    }
}
