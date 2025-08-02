package com.roha.movies;

import com.roha.movies.fetcher.*;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.Clock;

import static com.roha.movies.fetcher.Initializer.LONGTIMEAGO;

public class TestFixtures {

    public static MoviesDocumentParser LoadExternalDocumentLoader(String filename) throws IOException {
        ClassPathResource resource = new ClassPathResource(filename);
        String filenameResource = resource.getURL().toExternalForm();
        DocumentLoader documentLoader = DocumentLoader.create(filenameResource);
        MoviesDocumentParser moviesDocumentParser = new MoviesDocumentParser(documentLoader);
        return moviesDocumentParser;
    }
    public static MoviesFetcher createMoviesFetcher(String filename, Clock clock) throws IOException {
        if(filename == null) {
            filename = "overview_haarlem.html";
        }
        if(clock == null) {
            clock = LONGTIMEAGO;
        }
        MoviesDocumentParser moviesDocumentParser = TestFixtures.LoadExternalDocumentLoader(filename);
        MyMoviesRepository myMoviesRepository = new LocalMyMoviesRepositoryForTest();
        return new MoviesFetcher(moviesDocumentParser, myMoviesRepository, clock);

    }
}
