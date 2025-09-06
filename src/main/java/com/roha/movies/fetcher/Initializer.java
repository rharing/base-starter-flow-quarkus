package com.roha.movies.fetcher;

import com.google.common.base.Strings;
import com.roha.movies.domain.*;
import com.roha.movies.service.MailService;
import com.roha.movies.view.domain.MyMoviesAction;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;

@ApplicationScoped
public class Initializer implements WithLogger {

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
    @ConfigProperty(name = "access_key")
    String key;
    @ConfigProperty(name = "access_secret")
    String secret;
    private MoviesFetcher moviesFetcher;
    private MailService mailService;
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
            logger().info("got key: " + key);
            logger().info("got secret: " + secret);
            myMoviesRepository = new AwsMyMoviesRepository(bucket_name, region, key, secret);

            MyMovies myMovies = myMoviesRepository.load();
            if (myMovies.getWanted().isEmpty()) {
                logger().error("could not load myMovies from AWS");
                System.exit(801);
            }
        } else {
            Optional<String> externalUrl = baseDataLoader.getExternalUrl("my_movies.json");
            myMoviesRepository = new LocalMyMoviesRepository(externalUrl.get());
        }
        moviesFetcher = new MoviesFetcher(new MoviesDocumentParser(documentLoader), myMoviesRepository);

        if (!useLive) {
            moviesFetcher = new MoviesFetcher(new MoviesDocumentParser(documentLoader), new LocalMyMoviesRepositoryForTest());
            moviesFetcher.setClock(LONGTIMEAGO);
        }
//        updateMyMovies();
    }

    private void updateMyMovies() throws IOException {
        MyMovies myMovies = myMoviesRepository.load();
        locateMovieDetails(myMovies.getWanted());
        locateMovieDetails(myMovies.getSeen());
        locateMovieDetails(myMovies.getSkipped());
        myMoviesRepository.save(myMovies);
    }

    private Boolean locateMovieDetails(Map<String, MovieDTO> wanted) throws IOException {
        Boolean updated = false;
        for (MovieDTO movieDto : wanted.values()) {
            if (movieDto.href() != null && movieDto.image() == null) {
                Movie movie = loadMovie(movieDto);
                if (movie.imageHref() != null) {
                    movieDto = movie.asDTO();
                    wanted.put(movieDto.id(), movieDto);
                    updated = true;
                }
            }
        }
        return updated;
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

    public void updateMyMovie(MovieDTO movieDTO, MyMoviesAction action) throws IOException {
//        Movie movie = loadMovie(movieDTO);
        MyMovies myMovies = myMoviesRepository.load();
        if (action != null) {
            boolean updated = action.handle(myMovies, movieDTO);
            if (updated) {
                myMoviesRepository.save(myMovies);
            }
        }
    }

    public Movie loadMovie(MovieDTO movieDTO) throws IOException {
        if (fakeMovieContent) {
            String movieId = movieDTO.movieId();
            return new Movie(movieId, movieId, movieDTO.href(), "unknown", "bogus content for " + movieId, "", 42, new ArrayList<>(), "");
        } else {
            Movie movie = getMoviesFetcher().loadMovie(movieDTO);
            if (Strings.isNullOrEmpty(movie.content())) {
                // @todo mailservice is still null, replace with eventbus thingie
//                mailService.sendNoContentMovie(movie);
            }
            return movie;
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
