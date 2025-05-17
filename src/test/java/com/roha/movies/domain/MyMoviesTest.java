package com.roha.movies.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MyMoviesTest {

    @Test
    public void shouldAddWanted() {
        MyMovies myMovies = new MyMovies();
        MovieDTO normalFamily = new MovieDTO("a-perfectly-normal-family","a-perfectly-normal-family", "A Perfectly Normal Family","", "https://www.filmladder.nl/film/a-perfectly-normal-family-2020/popup/haarlem", "https://assets.filmladder.nl/uploads/depot_image/asset/000/953/383/953383/thumb_df47fdfdec18377a.jpg", "7",null, null);
        assertThat(myMovies.addWanted(normalFamily)).isTrue();
        assertThat(myMovies.addWanted(normalFamily)).isFalse();
        MovieDTO normalFamily2 = new MovieDTO("a-perfectly-normal-family-2","a-perfectly-normal-family-2", "A Perfectly Normal Family","", "https://www.filmladder.nl/film/a-perfectly-normal-family-2020/popup/haarlem", "https://assets.filmladder.nl/uploads/depot_image/asset/000/953/383/953383/thumb_df47fdfdec18377a.jpg", "7",null, null);
        assertThat(myMovies.addWanted(normalFamily2)).isTrue();
    }
}