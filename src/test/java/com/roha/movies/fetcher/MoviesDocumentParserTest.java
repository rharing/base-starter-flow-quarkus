package com.roha.movies.fetcher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roha.movies.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class MoviesDocumentParserTest {

    private MoviesDocumentParser moviesDocumentParser;

    @BeforeEach
    public void setUp() throws IOException {
        BaseDataLoader baseDataLoader = new BaseDataLoader();
        String filename = baseDataLoader.getExternalUrl("overview_haarlem.html").get();
        DocumentLoader documentLoader = DocumentLoader.create(filename);
        this.moviesDocumentParser = new MoviesDocumentParser(documentLoader);
    }

    @Test
    public void get_rid_of_weird_rating_char(){
        String convertedRating = this.moviesDocumentParser.convertRating("6.4*");
        assertThat(convertedRating,is("6.4"));
    }

    @Test
    public void shouldLocateCities() throws IOException {
        List<City> cities = moviesDocumentParser.loadCities();
        assertThat(cities, hasSize(109));
        ClassPathResource expectedCitiesFile = new ClassPathResource("cities.json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<City> expectedCities = objectMapper.readValue(expectedCitiesFile.getFile(), new TypeReference<>() {
        });
        assertThat(cities, IsIterableContainingInOrder.contains(expectedCities.toArray()));
    }

    @Test
    public void patheKungfu() throws IOException {
        MoviesFetcher moviesFetcher = new MoviesFetcher(moviesDocumentParser, new LocalMyMoviesRepositoryForTest(), Initializer.LONGTIMEAGO);
        List<PlayDTO> playDTOS = moviesFetcher.loadPlays(null);
        List<PlayDTO> dtoList = playDTOS.stream().filter(playDTO -> playDTO.cinema().contains("ath")).filter(playDTO -> playDTO.movieDTO().movieId().contains("kung-fu-panda-4-ov-2024")).toList();
        for (int i = 0; i < dtoList.size(); i++) {
            PlayDTO playDTO = dtoList.get(i);
            assertThat(playDTO.movieDTO().href(), is("https://www.filmladder.nl/film/kung-fu-panda-4-ov-2024/popup/haarlem"));
        }
    }
    @Test
    public void shouldLocateVoorstellingen() throws IOException {
        ClassPathResource resource = new ClassPathResource("when_istanbul.html");
        String filename = resource.getURL().toExternalForm();
        DocumentLoader documentLoader = DocumentLoader.create(filename);
        moviesDocumentParser = new MoviesDocumentParser(documentLoader);
        List<WhenPlayDTO> whenPlayDTOS = moviesDocumentParser.whenMovie(null);
        assertThat(whenPlayDTOS, hasSize(157));
        assertThat(whenPlayDTOS.get(154).city(), is("Zaandam"));
    }
    @Test
    public void shouldLocateVoorstellingenBambi() throws IOException {
        ClassPathResource resource = new ClassPathResource("when_bambi.html");
        String filename = resource.getURL().toExternalForm();
        DocumentLoader documentLoader = DocumentLoader.create(filename);
        moviesDocumentParser = new MoviesDocumentParser(documentLoader);
        List<WhenPlayDTO> whenPlayDTOS = moviesDocumentParser.whenMovie(null);
        List<WhenPlayDTO> bambiInHaarlem = whenPlayDTOS.stream().filter(whenPlayDTO -> whenPlayDTO.city().equalsIgnoreCase("haarlem")).toList();
        assertThat(bambiInHaarlem, hasSize(2));
        assertThat(bambiInHaarlem.get(0).start(),is(notNullValue()));
        assertThat(bambiInHaarlem.get(0).start().toString(),is("2024-11-10T14:15"));
        assertThat(bambiInHaarlem.get(0).end(),is(notNullValue()));
        assertThat(bambiInHaarlem.get(0).end().toString(),is("2024-11-10T15:45"));

    }

    @Test
    public void shouldLocateMovies() throws IOException {
        List<Movie> movies = moviesDocumentParser.locateMoviesWithPlays(null);
        assertThat(movies, hasSize(93));
        List<Movie> schuurMovies = movies.stream().filter(movie -> movie.plays().stream().anyMatch(play -> play.cinema().equalsIgnoreCase("schuur"))).toList();
        assertThat(schuurMovies, hasSize(16));
        Movie movieTigerStripes = schuurMovies.get(15);
        assertThat(movieTigerStripes.title(), is("Tiger Stripes"));
        assertThat(movieTigerStripes.plays(), hasSize(3));
    }

    @Test
    public void shouldLocateEndTijdEnDuration() {
        String title = "Koop een kaartje voor A Streetcar Named Desire in FilmKoepel. Van 16:00 tot 18:15";
        LocalDateTime start = LocalDateTime.of(2024, 5, 19, 16, 0, 0);
        Movie movie = new Movie("id","title","href","rating","content","imageHref", 0, new ArrayList<>(), "");

        movie = movie.withDuration(start, title);
        assertThat(movie.duration(), is(135));
    }

    @Test
    public void bugBinnensteBuitenPlays() throws IOException {
        ClassPathResource resource = new ClassPathResource("binnenstebuitenBug.html");
        String filename = resource.getURL().toExternalForm();
        DocumentLoader documentLoader = DocumentLoader.create(filename);
        moviesDocumentParser = new MoviesDocumentParser(documentLoader);
        List<Movie> movies = moviesDocumentParser.locateMoviesWithPlays(null);
        Movie binnenstebuiten = movies.stream().filter(movie -> movie.title().contains("innenstebuiten 2 (NL)")).findFirst().get();

        List<Play> patheBinnensteBuiten = movies.stream().map(movie -> movie.plays()).flatMap(List::stream)
                .collect(Collectors.toList()).stream().filter(play -> play.cinema().startsWith("Path") && play.movie().title().contains("Binnenste")).toList();
        assertThat(patheBinnensteBuiten, hasSize(4));
        assertThat(patheBinnensteBuiten.get(0).titleAddOn(), is("2D"));
        assertThat(patheBinnensteBuiten.get(0).id(), is("binnenstebuiten-2-nl-2024_09-02_16:00"));
        assertThat(patheBinnensteBuiten.get(1).titleAddOn(), is("2D"));
        assertThat(patheBinnensteBuiten.get(1).id(), is("binnenstebuiten-2-nl-2024_09-03_14:45"));
        assertThat(patheBinnensteBuiten.get(2).titleAddOn(), is("2D"));
        assertThat(patheBinnensteBuiten.get(2).id(), is("binnenstebuiten-2-nl-2024_09-04_16:00"));
        assertThat(patheBinnensteBuiten.get(3).titleAddOn(), is("3D"));
        assertThat(patheBinnensteBuiten.get(3).id(), is("binnenstebuiten-2-nl-2024_09-04_14:20"));
    }

    @Test
    @Tag("LiveTest")
    public void shouldLocateLiveCities() throws IOException {

        this.moviesDocumentParser = new MoviesDocumentParser(null);
        List<City> cities = moviesDocumentParser.loadCities();
        assertThat(cities.size(), greaterThan(100));
//        assertThat(cities, hasSize(109));
//        ClassPathResource expectedCitiesFile = new ClassPathResource("/cities.json");
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        List<City> expectedCities = objectMapper.readValue(expectedCitiesFile.getFile(), new TypeReference<>() {
//        });
//        assertThat(cities, IsIterableContainingInOrder.contains(expectedCities.toArray()));

    }

    @Test
    public void shouldHandleSpecialCharacters() throws IOException {
        List<Movie> movies = moviesDocumentParser.locateMoviesWithPlays(null);
        Assertions.assertThat(movies).hasSize(93);

        List<Movie> patheMovies = movies.stream().filter(movie -> movie.plays().stream().anyMatch(play -> play.cinema().startsWith("Path"))).toList();
        String cinema = patheMovies.get(0).plays().get(0).cinema();
        String patheHaarlemNoAccents = "Pathe Haarlem";
        if (StringUtils.stripAccents(cinema).equals(patheHaarlemNoAccents)) {
            // yeah works but not the assertThat
//            assertThat(cinema).isEqualTo("Path� Haarlem");
        } else {
            org.junit.jupiter.api.Assertions.fail("cinema isnt Path Haarlem");
        }
//        Assertions.assertThat(cinema).isEqualTo("Pathé Haarlem");

    }

}