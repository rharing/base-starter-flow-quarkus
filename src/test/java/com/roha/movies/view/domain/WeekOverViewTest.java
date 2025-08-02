package com.roha.movies.view.domain;

import com.roha.movies.TestFixtures;
import com.roha.movies.domain.Movie;
import com.roha.movies.domain.Play;
import com.roha.movies.domain.WhenPlayDTO;
import com.roha.movies.fetcher.MoviesDocumentParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

class WeekOverViewTest {

    @Test
    public void shouldFindVandaag() throws IOException {
        MoviesDocumentParser moviesDocumentParser = TestFixtures.LoadExternalDocumentLoader("31-mei.html");
        List<WhenPlayDTO> whenPlayDTOS = moviesDocumentParser.whenMovie(null);
        List<WhenPlayDTO> unparseableEndTimes = whenPlayDTOS.stream().filter(whenPlayDTO -> whenPlayDTO.start().equals(whenPlayDTO.end())).toList();
        assertThat(unparseableEndTimes, hasSize(1));
        List<Movie> movies = moviesDocumentParser.locateMoviesWithPlays(null);
        WeekOverView weekOverview = new WeekOverView();
        movies.stream().filter(movie -> movie.title().equals("Mission: Impossible – The Final Reckoning")).forEach(movie -> {
            List<Play> plays = movie.plays();
            weekOverview.addPlays(plays);
        });
        assertThat(weekOverview.getCinemas(), hasSize(3));
        // start with saturday
        LocalDate when = LocalDateTime.of(2025, Month.MAY, 31, 0, 0).toLocalDate();

        List<DayOverview> overview = weekOverview.getOverview(when);
        testWeek(overview, Arrays.asList("vandaag", "morgen", "maandag", "dinsdag", "woensdag", "donderdag", "vrijdag"));
        weekOverview.resetDagen();
        when = LocalDateTime.of(2025, Month.MAY, 30, 0, 0).toLocalDate(); //vrijdag
        overview = weekOverview.getOverview(when);
        testWeek(overview, Arrays.asList("vandaag", "morgen", "zondag", "maandag", "dinsdag", "woensdag", "donderdag"));
        weekOverview.resetDagen();
        when = LocalDateTime.of(2025, Month.JUNE, 1, 0, 0).toLocalDate(); //Zondag
        overview = weekOverview.getOverview(when);
        testWeek(overview, Arrays.asList("vandaag", "morgen", "dinsdag", "woensdag", "donderdag", "vrijdag", "zaterdag"));
    }

    private void testWeek(List<DayOverview> daysOverview, List<String> expected) {
        List<String> alleDagen = new ArrayList<>();
        daysOverview.forEach(dayOverview -> alleDagen.add(dayOverview.getDag()));
//        assertThat(alleDagen, IsIterableContainingInOrder.contains(expected));
        for (int i = 0; i < alleDagen.size(); i++) {
            String dag = alleDagen.get(i);
            assertThat(dag, is(expected.get(i)));
        }
    }

}