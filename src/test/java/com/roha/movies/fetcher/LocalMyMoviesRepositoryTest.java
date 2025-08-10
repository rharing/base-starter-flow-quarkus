package com.roha.movies.fetcher;

import com.roha.movies.domain.MovieDTO;
import com.roha.movies.domain.MyMovies;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

class LocalMyMoviesRepositoryTest {

    @Test
    public void shouldReadMyMovies() {
        LocalMyMoviesRepository localMyMoviesRepository = new LocalMyMoviesRepository();
        MyMovies myMovies = localMyMoviesRepository.load();
        assertThat(myMovies).isNotNull();
        assertThat(myMovies.getWanted()).hasSize(59);
    }

    @Test
    public void shouldNotFailOnEmptyFields() throws IOException {
        MyMoviesRepository localMyMoviesRepository = null;
        try {
            localMyMoviesRepository = new LocalMyMoviesRepositoryForTest();
            MyMovies myMovies = localMyMoviesRepository.load();
            MovieDTO normalFamily = new MovieDTO("a-perfectly-normal-family","a-perfectly-normal-family", "A Perfectly Normal Family","", "https://www.filmladder.nl/film/a-perfectly-normal-family-2020/popup/haarlem", "https://assets.filmladder.nl/uploads/depot_image/asset/000/953/383/953383/thumb_df47fdfdec18377a.jpg", "",null, null);
            assertThat(myMovies.addWanted(normalFamily)).isTrue();
            MovieDTO normalFamily2 = new MovieDTO("a-perfectly-normal-family-2","a-perfectly-normal-family-2", "A Perfectly Normal Family","", "https://www.filmladder.nl/film/a-perfectly-normal-family-2020/popup/haarlem", "https://assets.filmladder.nl/uploads/depot_image/asset/000/953/383/953383/thumb_df47fdfdec18377a.jpg", "3 sterren",null, null);
            assertThat(myMovies.addWanted(normalFamily2)).isTrue();
            localMyMoviesRepository.save(myMovies);
            myMovies = localMyMoviesRepository.load();
            assertThat(myMovies).isNotNull();
            assertThat(myMovies.getWanted().values()).hasSize(2);
            assertThat(myMovies.getWanted().get("a-perfectly-normal-family").rating()).isEmpty();
        } finally {
            if (localMyMoviesRepository != null) {
                localMyMoviesRepository.clean();
            }
        }


    }

    @Test
    public void shouldSaveMyMovies() throws IOException {
        MyMoviesRepository localMyMoviesRepository = null;
        try {
            localMyMoviesRepository = new LocalMyMoviesRepositoryForTest();
            MyMovies myMovies = localMyMoviesRepository.load();
            MovieDTO normalFamily = new MovieDTO(
                    "a-perfectly-normal-family",
                    "a-perfectly-normal-family",
                    "A Perfectly Normal Family",
                    "",
                    "https://www.filmladder.nl/film/a-perfectly-normal-family-2020/popup/haarlem",
                    "https://assets.filmladder.nl/uploads/depot_image/asset/000/953/383/953383/thumb_df47fdfdec18377a.jpg",
                    "7",
                    null, null);
            myMovies.addWanted(normalFamily);
            MovieDTO normalFamily2 = normalFamily.withMovieId("a-perfectly-normal-family-2");
            myMovies.addWanted(normalFamily2);
            localMyMoviesRepository.save(myMovies);
            myMovies = localMyMoviesRepository.load();
            assertThat(myMovies).isNotNull();
            assertThat(myMovies.getWanted().values()).hasSize(2);
            assertThat(myMovies.getSeen().values()).hasSize(0);
        } finally {
            if (localMyMoviesRepository != null) {
                localMyMoviesRepository.clean();
            }
        }

    }

}