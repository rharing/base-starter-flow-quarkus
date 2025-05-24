package com.roha.movies;

import com.roha.movies.domain.City;
import com.roha.movies.domain.Movie;
import com.roha.movies.domain.MovieDTO;
import com.roha.movies.domain.PlayDTO;
import com.roha.movies.fetcher.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.InstantSource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.roha.movies.fetcher.Initializer.LONGTIMEAGO;
import static org.assertj.core.api.Assertions.assertThat;


class MoviesFetcherTest {
    @Test
    public void shouldLocateCities() throws IOException {
        BaseDataLoader baseDataLoader = new BaseDataLoader();
        DocumentLoader documentLoader = DocumentLoader.create(baseDataLoader.getExternalUrl("overview_haarlem.html").get());

        MoviesFetcher moviesFetcher = new MoviesFetcher(documentLoader);
        List<City> cities = moviesFetcher.loadCities();
        assertThat(cities).hasSize(109);
    }

    @Test
    public void checkingSpecialChars() throws IOException {
        BaseDataLoader baseDataLoader = new BaseDataLoader();
        DocumentLoader documentLoader = DocumentLoader.create(baseDataLoader.getExternalUrl("overview_haarlem.html").get());
        MoviesDocumentParser moviesDocumentParser = new MoviesDocumentParser(documentLoader);
        // as overview is in the past, use a different clock to fetch movies, this will make none of the movies too late
        Clock clock = LONGTIMEAGO;
        MoviesFetcher moviesFetcher = new MoviesFetcher(moviesDocumentParser, new LocalMyMoviesRepositoryForTest(), clock);
        List<PlayDTO> plays = null;
        try {
            plays = moviesFetcher.loadPlays(null);
            assertThat(plays).hasSize(403);
            List<PlayDTO> patheFilms = plays.stream().filter(play -> play.cinema().startsWith("Path")).toList();
            assertThat(patheFilms).hasSize(160);
            assertThat(patheFilms.get(0).cinema().equals("Path� Haarlem"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testPlayComparator() {

    }

    @Test
    public void checkingSpecialCharsLive() throws IOException {
        DocumentLoader documentLoader = DocumentLoader.create("https://filmladder.nl/");
        MoviesDocumentParser moviesDocumentParser = new MoviesDocumentParser(documentLoader);
        // as overview is in the past, use a different clock to fetch movies, this will make none of the movies too late
        Clock clock = LONGTIMEAGO;

        MoviesFetcher moviesFetcher = new MoviesFetcher(moviesDocumentParser, new LocalMyMoviesRepositoryForTest(), clock);
        List<PlayDTO> plays = null;
        try {
            plays = moviesFetcher.loadPlays("haarlem");
            assertThat(plays.size()).isGreaterThan(0);

            List<PlayDTO> patheFilms = plays.stream().filter(play -> play.cinema().startsWith("Path")).toList();
            assertThat(patheFilms.size()).isGreaterThan(0);
            assertThat(patheFilms.get(0).cinema().equals("Pathé Haarlem"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void completeFlow() throws IOException {
        BaseDataLoader baseDataLoader = new BaseDataLoader();
        DocumentLoader documentLoader = DocumentLoader.create(baseDataLoader.getExternalUrl("overview_haarlem.html").get());
        MoviesDocumentParser moviesDocumentParser = new MoviesDocumentParser(documentLoader);
        // as overview is in the past, use a different clock to fetch movies, this will make none of the movies too late
        Clock clock = (Clock) InstantSource.fixed(Instant.parse("2024-04-13T08:00:00+02:00"));
        MyMoviesRepository myMoviesRepository = new LocalMyMoviesRepositoryForTest();
        MoviesFetcher moviesFetcher = new MoviesFetcher(moviesDocumentParser, myMoviesRepository, clock);
        try {
            List<PlayDTO> plays = moviesFetcher.loadPlays(null);
            assertThat(plays).hasSize(403);

            List<PlayDTO> schuurPlays = plays.stream().filter(play -> play.cinema().equalsIgnoreCase("schuur")).toList();
            assertThat(schuurPlays).hasSize(41);
            assertThat(schuurPlays.get(0).id()).isEqualTo("evil-does-not-exist-2023_04-14_12:30");
            assertThat(schuurPlays.get(0).movieDTO().movieId()).isEqualTo("evil-does-not-exist-2023");
            assertThat(schuurPlays.get(0).movieDTO().href()).isEqualTo("https://www.filmladder.nl/film/evil-does-not-exist-2023/popup/haarlem");
            moviesFetcher.seenMovie("als-ik-mijn-ogen-sluit-2024");
            plays = moviesFetcher.loadPlays(null);
            assertThat(plays).hasSize(401);

            schuurPlays = plays.stream().filter(play -> play.cinema().equalsIgnoreCase("schuur")).toList();
            assertThat(schuurPlays).hasSize(39);

            // now we are a bit later wo 17-4-2024
            clock = (Clock) InstantSource.fixed(Instant.parse("2024-04-17T08:00:00+02:00"));
            moviesFetcher = new MoviesFetcher(moviesDocumentParser, myMoviesRepository, clock);
            plays = moviesFetcher.loadPlays(null);
            schuurPlays = plays.stream().filter(play -> play.cinema().equalsIgnoreCase("schuur")).toList();
            assertThat(schuurPlays).hasSize(11);
            assertThat(schuurPlays.get(0).movieDTO().movieId()).contains("the-peasants");

        } finally {
            myMoviesRepository.clean();
        }
    }

    @Nested
    public class LiveTests {

        @Test
        @Tag("liveTests")
        public void shouldLoadCities() throws IOException {
            MoviesFetcher moviesFetcher = new MoviesFetcher(DocumentLoader.create("http://www.filmladder.nl"));
            List<City> cities = moviesFetcher.loadCities();
            assertThat(cities).isNotEmpty();
        }

        @Test
        @Tag("liveTests")
        public void shouldLoadMovieBeforeSunset() throws IOException {
            ClassPathResource resource = new ClassPathResource("oppenheimer.html");
            String filename = resource.getURL().toExternalForm();
            DocumentLoader documentLoader = DocumentLoader.create(filename);
            MoviesDocumentParser moviesDocumentParser = new MoviesDocumentParser(documentLoader);
            // as overview is in the past, use a different clock to fetch movies, this will make none of the movies too late
            Clock clock = LONGTIMEAGO;
            MyMoviesRepository myMoviesRepository = new LocalMyMoviesRepositoryForTest();
            MoviesFetcher moviesFetcher = new MoviesFetcher(moviesDocumentParser, myMoviesRepository, clock);

            Movie movie = moviesFetcher.loadMovie(new MovieDTO("movieId", "movieId", "title", "", "image", "https://www.filmladder.nl/film/before-sunset-2004", "rating", null, null));
            assertThat(movie.content()).isEqualTo("Negen jaar nadat Jesse en Celine een paar onvergetelijke uren in Wenen hebben doorgebracht komen ze elkaar opnieuw tegen in Parijs. Jesse is inmiddels schrijver en in Parijs in een boekhandel om zijn boek te promoten. Dan ziet hij Celine staan. Al snel blijkt hoeveel indruk hun eerste ontmoeting heeft achtergelaten. Opnieuw raken zij in gesprek en brengen ze wandelend en pratend met elkaar door en opnieuw is er een tikkende klok voor Jesse's vliegtuig vertrekt. Before Sunset is het vervolg op Before Sunrise uit 1995.");
            assertThat(movie.title()).isEqualTo("Before Sunset");
            assertThat(movie.duration()).isEqualTo(80);
            assertThat(movie.rating()).isEqualTo("8.1");
            assertThat(movie.imageHref()).contains("small_62fa19b2ea5ac914.jpg");

        }
    }

    @Nested
    class LoadingMovieContent {

        @Test
        public void HappyPath() throws IOException {

            ClassPathResource resource = new ClassPathResource("before-sunset.html");
            String filename = resource.getURL().toExternalForm();
            DocumentLoader documentLoader = DocumentLoader.create(filename);
            MoviesDocumentParser moviesDocumentParser = new MoviesDocumentParser(documentLoader);
            // as overview is in the past, use a different clock to fetch movies, this will make none of the movies too late
            Clock clock = (Clock) InstantSource.fixed(Instant.parse("2024-04-13T08:00:00+02:00"));
            MyMoviesRepository myMoviesRepository = new LocalMyMoviesRepositoryForTest();
            MoviesFetcher moviesFetcher = new MoviesFetcher(moviesDocumentParser, myMoviesRepository, clock);
            Movie movie = moviesFetcher.loadMovie();
            assertThat(movie.content()).isEqualTo("Negen jaar nadat Jesse en Celine een paar onvergetelijke uren in Wenen hebben doorgebracht komen ze elkaar opnieuw tegen in Parijs. Jesse is inmiddels schrijver en in Parijs in een boekhandel om zijn boek te promoten. Dan ziet hij Celine staan. Al snel blijkt hoeveel indruk hun eerste ontmoeting heeft achtergelaten. Opnieuw raken zij in gesprek en brengen ze wandelend en pratend met elkaar door en opnieuw is er een tikkende klok voor Jesse's vliegtuig vertrekt. Before Sunset is het vervolg op Before Sunrise uit 1995.");
            assertThat(movie.title()).isEqualTo("Before Sunset");
            assertThat(movie.duration()).isEqualTo(80);
            assertThat(movie.rating()).isEqualTo("8.1");
            assertThat(movie.imageHref()).isEqualTo("https://assets.filmladder.nl/uploads/imdb_poster/asset/000/003/915/3915/small_62fa19b2ea5ac914.jpg");


        }

        @Test
        public void shouldNotFailOnMissingRating() throws IOException {
            ClassPathResource resource = new ClassPathResource("als-ik-mijn-ogen-sluit.html");
            String filename = resource.getURL().toExternalForm();
            DocumentLoader documentLoader = DocumentLoader.create(filename);
            MoviesDocumentParser moviesDocumentParser = new MoviesDocumentParser(documentLoader);
            // as overview is in the past, use a different clock to fetch movies, this will make none of the movies too late
            Clock clock = (Clock) InstantSource.fixed(Instant.parse("2024-04-13T08:00:00+02:00"));
            MyMoviesRepository myMoviesRepository = new LocalMyMoviesRepositoryForTest();
            MoviesFetcher moviesFetcher = new MoviesFetcher(moviesDocumentParser, myMoviesRepository, clock);
            Movie movie = moviesFetcher.loadMovie();
            assertThat(movie.content()).isEqualTo("Als ik mijn ogen sluit is een documentaire over de vrouwen en meisjes die de Japanse kampen overleefden en hoe zij hier later in hun leven mee omgingen. De Japanse kampen lieten grote littekens na en veel van die littekens zijn nog niet genezen of verdwenen. Als de vrouwen hun ogen sluiten, komen deze verhalen bovendrijven. Wij als kijker kruipen hiermee in de hoofden van de vrouwen die de kampen hebben overleefd. Van de kampen bestaan weinig foto's en er is haast geen filmmateriaal. Wel zijn er door de vrouwen en kinderen in het kamp honderden tekeningen gemaakt.");
            assertThat(movie.title()).isEqualTo("Als ik mijn ogen sluit");
            assertThat(movie.imageHref()).isEqualTo("https://assets.filmladder.nl/uploads/depot_image/asset/001/033/876/1033876/small_848c20c143209cd6.jpg");
            assertThat(movie.duration()).isEqualTo(95);
            assertThat(movie.rating()).isEqualTo("");

        }
    }

    @Test
    public void sortingPlays() throws IOException {
        BaseDataLoader baseDataLoader = new BaseDataLoader();
        DocumentLoader documentLoader = DocumentLoader.create(baseDataLoader.getExternalUrl("overview_haarlem.html").get());

        MoviesDocumentParser moviesDocumentParser = new MoviesDocumentParser(documentLoader);
        Clock clock = LONGTIMEAGO;
        MoviesFetcher moviesFetcher = new MoviesFetcher(moviesDocumentParser, new LocalMyMoviesRepositoryForTest(), clock);
        List<PlayDTO> haarlem = moviesFetcher.loadPlays("haarlem");

        assertThat(haarlem.get(0).id()).isEqualTo("kung-fu-panda-4-ov-2024_04-14_09:30");
        assertThat(haarlem.get(0).movieDTO().href()).isEqualTo("https://www.filmladder.nl/film/kung-fu-panda-4-ov-2024/popup/haarlem");
    }

    @Test
    void shouldShowCorrectPlays() throws IOException {
        Movie movie = new Movie("someMovie", "someMovie", "someMovie", "someMovie", "someMovie", "someMovie", 25, new ArrayList<>(), "");
        LocalDateTime when = LocalDateTime.of(2024, 4, 20, 18, 20);
        Clock clock = (Clock) InstantSource.fixed(Instant.parse("2024-04-20T18:20:00+00:00"));
        movie.addPlay(when.plusMinutes(3), "firstPlay", "firstPlay", "schuur", "");
        MyMoviesRepository myMoviesRepository = new LocalMyMoviesRepositoryForTest();
        try {
            ExternalDocumentLoader documentLoader = new ExternalDocumentLoader("");
            MoviesDocumentParser moviesDocumentParser = new MoviesDocumentParser(documentLoader) {
                @Override
                public List<Movie> locateMoviesWithPlays(String city) throws IOException {
                    return List.of(movie);
                }
            };
            MoviesFetcher moviesFetcher = new MoviesFetcher(moviesDocumentParser, myMoviesRepository, clock);

            List<PlayDTO> plays = moviesFetcher.loadPlays(null);
            assertThat(plays).hasSize(1);
            movie.addPlay(when.minusMinutes(3), "secondPlay", "secondPlay", "schuur", "");
//            Mockito.when(moviesDocumentParser.locateMoviesWithPlays(null)).thenReturn(List.of(movie));
            plays = moviesFetcher.loadPlays(null);
            assertThat(plays).hasSize(1);
            assertThat(plays.get(0).id()).isEqualTo("someMovie_04-20_18:23");

            MovieDTO someMovieDTO = new MovieDTO("someMovie", "someMovie", "someMovie", "someMovie", "someMovie", "someMovie", "", null, 25);
            moviesFetcher.addSeen(someMovieDTO);
            plays = moviesFetcher.loadPlays(null);
            assertThat(plays).hasSize(0);
            moviesFetcher.reset(someMovieDTO);
            plays = moviesFetcher.loadPlays(null);
            assertThat(plays).hasSize(1);
        } finally {
            myMoviesRepository.clean();
        }

    }

    @Test
    public void delme() {
        /*

         <"vos-en-haas-redden-het-bos-2024-352618">
                to be equal to:
 <"kung-fu-panda-4-ov-2024_04-14_09:30">
                but was not.
                         */


        String vos = "vos-en-haas-redden-het-bos-2024-352618";
        String kung = "kung-fu-panda-4-ov-2024_04-14_09:30";
        List<String> movies = new ArrayList<>();
        movies.add(vos);
        movies.add(kung);
        Collections.sort(movies);
        assertThat(movies.get(0)).isEqualTo(kung);
    }

}