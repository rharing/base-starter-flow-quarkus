package com.roha.movies.fetcher;

import com.roha.movies.config.BaseDataLoader;
import com.roha.movies.domain.Movie;
import com.roha.movies.domain.MovieDTO;
import com.roha.movies.domain.PlayDTO;
import jakarta.enterprise.context.Dependent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Dependent
public class Initializer {

    @ConfigProperty(name = "useLive",defaultValue = "true")
    Boolean useLive;
    @ConfigProperty(name = "fakeMovieContent",defaultValue = "true")
    Boolean fakeMovieContent;
    @ConfigProperty(name ="useAws", defaultValue = "false")
    Boolean useAws;
    @ConfigProperty(name = "s3_bucket")
    String bucket_name;
    @ConfigProperty(name ="s3_region")
    String region;
    private MoviesFetcher moviesFetcher;
    private MyMoviesRepository myMoviesRepository;


    private com.roha.movies.fetcher.BaseDataLoader baseDataLoader = new BaseDataLoader();
    public static final Clock LONGTIMEAGO = Clock.fixed(
            Instant.parse("2018-04-11T20:34:58Z"),
            ZoneOffset.UTC);

    @EventListener(ApplicationReadyEvent.class)
    void onStart() throws IOException {
        DocumentLoader documentLoader;
        if (useLive) {
            documentLoader = new ExternalDocumentLoader("https://www.filmladder.nl/");
        } else {
            Optional<String> optionalUrl = baseDataLoader.getExternalUrl("overview_haarlem.html");
            if(optionalUrl.isPresent()) {
                documentLoader = new DocumentLoaderFromFile(optionalUrl.get());
            }
            else{
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

    public Movie loadMovie(MovieDTO movieDTO) throws IOException {
        if (fakeMovieContent) {
            String movieId = movieDTO.getMovieId();
            return Movie.builder().id(movieId).href(movieDTO.getHref())
                    .content("bogus content for " + movieId)
                    .rating("unknown")
                    .title(movieId)
                    .titleAddOn("")
                    .duration(42).imageHref("").build();
        } else {
            return getMoviesFetcher().loadMovie(movieDTO);
        }
    }
}
