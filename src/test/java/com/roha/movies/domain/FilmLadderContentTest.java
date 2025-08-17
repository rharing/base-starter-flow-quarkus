package com.roha.movies.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.FileReader;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class FilmLadderContentTest {

    @Test
    public void shouldMapOk() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("filmLadderContent.json");
        String json = FileCopyUtils.copyToString(new FileReader(classPathResource.getFile()));

        ObjectMapper objectMapper = new ObjectMapper();
        FilmLadderContent filmLadderContent = objectMapper.readValue(json, FilmLadderContent.class);
        assertThat(filmLadderContent.getName()).isEqualTo("Peacock");
        assertThat(filmLadderContent.getAggregateRating().getRatingValue()).isEqualTo(7.2f);
        assertThat(filmLadderContent.loadDuration()).isEqualTo(102);
        assertThat(filmLadderContent.getDescription()).startsWith("Matthias is de perfecte +1. Ben je op zoek naar de perfecte zoon om indruk mee te maken? Een gesprekspartner die de juiste wijn kent en de juiste boeken heeft gelezen? Of zoek je iemand om mee te leren ruzi");
    }
}