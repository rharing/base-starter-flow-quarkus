package com.roha.movies.fetcher;

import com.roha.movies.domain.City;
import com.roha.movies.domain.IdCreator;
import com.roha.movies.domain.Movie;
import com.roha.movies.domain.WhenPlayDTO;
import com.roha.movies.fetcher.DocumentLoader;
import com.roha.movies.fetcher.ExternalDocumentLoader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoviesDocumentParser {

    DocumentLoader documentLoader;

    Pattern vanTot = Pattern.compile("Van (.)*? tot (.)*?");

    public MoviesDocumentParser(DocumentLoader documentLoader) {
        this.documentLoader = documentLoader;
    }

    public List<City> loadCities() throws IOException {
        if (documentLoader == null) {
            documentLoader = DocumentLoader.create("http://www.filmladder.nl");
        }
        return _loadCities();
    }

    public List<City> _loadCities() throws IOException {
        List<City> cities = new ArrayList<>();
        Document document = documentLoader.parse();
        Elements select = document.select("div.cities-sheet>a");
        for (Element element : select) {
            String href = locateNodeValue(element, "href");
            String name = element.text();
            if (href != null && name != null) {
                City city = new City.CityBuilder().href(href).name(name).build();
                cities.add(city);
            }
        }
        return cities;
    }

    private String locateNodeValue(Element element, String href) {
        return element.getAllElements().stream().filter(element1 -> {
            return element1.attributes().hasKeyIgnoreCase(href);
        }).findFirst().map(value -> value.attr(href)).orElse(null);
    }

    public List<Movie> locateMoviesWithPlays(String city) throws IOException {
        List<Movie> movies = new ArrayList<>();
        if (documentLoader instanceof ExternalDocumentLoader && city != null) {
            documentLoader = new ExternalDocumentLoader("https://www.filmladder.nl/" + city);
        }
        Document document = documentLoader.parse();
        Elements select = document.select("div.cinema-name");
        for (Element element : select) {
            String cinema = element.select("a.cinema-link").text();
            movies.addAll(locateLocateMoviesWithPlaysPerCinema(element, cinema));
        }
        return movies;
    }

    private List<Movie> locateLocateMoviesWithPlaysPerCinema(Element element, String cinema) {
        List<Movie> movies = new ArrayList<>();
        Elements moviesPerCinema = element.parent().select("div.movies>div");
        for (Element movieElement : moviesPerCinema) {
            Element movieLink = movieElement.select("a.text-link").get(0);

            String movieId = movieElement.attr("class").split("hall ")[1];
            String title = movieLink.attr("title");
            Elements titleAddOn = movieLink.select("span");
            String href = movieElement.select("a.text-link").get(0).attr("href");
//            Movie.MovieBuilder movieBuilder = Movie.builder().title(title).id(movieId).href(href).plays(new ArrayList<>());
            String imageHref = movieElement.select("img").attr("data-src");
            String rating = movieElement.select("span.star-rating>a.movie-link").text();
            if(rating.length()>=3) {
                // get rid of weird chars
                rating = convertRating(rating);
            }
            Movie movie = new Movie(movieId, title, href, rating, null, imageHref, null, new ArrayList<>(), null);

            Optional<Movie> optionalExistingMovie = movies.stream().filter(existingMovie -> existingMovie.title().equals(title)).findFirst();
            if (optionalExistingMovie.isPresent()) {
                movie = optionalExistingMovie.get();
            }
            Elements plays = movieElement.select("div[itemprop='startDate']>a");
            for (Element playLink : plays) {
                String startAt = playLink.parent().attr("content");
                LocalDateTime startDate = LocalDateTime.parse(startAt, DateTimeFormatter.ISO_DATE_TIME);
                String titletimes = playLink.attr("title");
                if (movie.duration() == null) {
                    movie = movie.withDuration(startDate, titletimes);
                }
                movie.addPlay(startDate, titletimes, playLink.attr("href"), cinema, titleAddOn.text());
            }
            if (optionalExistingMovie.isEmpty()) {
                movies.add(movie);
            }
        }
        return movies;
    }

    String convertRating(String text) {
        char[] chars = text.toCharArray();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if(aChar >='0' && aChar <='9' || aChar == '.' ) {
                result.append(aChar);
            }
        }
        return result.toString();
    }

    public Movie loadMovie(String href) throws IOException {
        if (href != null) {
            documentLoader = new ExternalDocumentLoader(href);
        }
        Document document = documentLoader.parse();
        String content = document.select("p.synopsis").text();

        String title = document.select("div#short-details>h3").attr("title");
        String movieId = IdCreator.create(title);
        if (title.startsWith("Details ")) {
            title = title.replace("Details ", "");
        }
        final Elements durationElement = document.select("p[itemprop=duration]");
        String duration = "";
        if (durationElement != null) {
            duration = durationElement.text();
            if (duration == null) {
                duration = "";
            }
        }
        Integer minuten =0;
        if (duration.endsWith("minuten")) {
            minuten = Integer.valueOf(duration.replace("minuten", "").strip());
        }

        String rating = document.select("span[itemprop=ratingValue]").text();
        if (rating == null) {
            rating = "";
        }

        String imageHref = document.select("img.poster").attr("src");
        return new Movie(movieId, title, href, rating, content, imageHref, minuten, new ArrayList<>(), null);
    }

    public List
            <WhenPlayDTO> whenMovie(String id) throws IOException {
        if (documentLoader instanceof ExternalDocumentLoader && id != null) {
            // hmm looks like this voorstellingen url us not maintained anymore as i can not find the link on the site
            // so alternatively i could loop over all the cities
            documentLoader = new ExternalDocumentLoader("https://www.filmladder.nl/film/" + id + "/voorstellingen");
        }
        Document document = documentLoader.parse();
        Elements cities = document.select("div.city");
        List<WhenPlayDTO> allTimes = new ArrayList<>();
        for (Element city : cities) {
            String cityName = city.select("h3>a").text();
            for (Element cinemaElement : city.select("div.hall")) {
                String cinema = cinemaElement.select("a.cinema-link").attr("title");

                final Elements plays = cinemaElement.select("div[itemprop=startDate]");
                for (Element play : plays) {
                    String startAt = play.attr("content");
                    LocalDateTime startDate = LocalDateTime.parse(startAt, DateTimeFormatter.ISO_DATE_TIME);
                    final Element ticketNode = play.selectFirst("a");
                    Elements titleElement = play.select("a");
                    String title = titleElement.attr("title");
                    Matcher matcher = vanTot.matcher(title);
                    LocalDateTime endDate = startDate;
                    if(matcher.find()){
                        String fields[] = title.split("Van ");
                        if(fields.length== 2){
                            String[] vanTot = fields[1].split("tot");
                            String van = vanTot[0].strip();
                            String tot = vanTot[1].strip();
                            String end = startAt.replace(van, tot);
                            endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME);
                        }
                    }

                    String ticketHref = null;
                    if (ticketNode != null) {
                        ticketHref = ticketNode.attr("href");
                    }
                    WhenPlayDTO whenPlayDTO = new WhenPlayDTO(cityName, startDate, endDate, ticketHref, cinema);
                    allTimes.add(whenPlayDTO);
                }
            }
        }
        return allTimes;
    }
}

