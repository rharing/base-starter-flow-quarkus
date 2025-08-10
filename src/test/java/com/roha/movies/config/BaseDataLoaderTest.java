package com.roha.movies.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.roha.movies.domain.MyMovies;
import com.roha.movies.domain.PlayDTO;
import com.roha.movies.fetcher.BaseDataLoader;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class BaseDataLoaderTest {

    BaseDataLoader baseDataLoader = new BaseDataLoader();

    @Test
    public void shouldLocateMyBaseData() throws IOException {
        String url = baseDataLoader.getUrl("all_plays.json").get();
        String jsonContent = FileCopyUtils.copyToString(new FileReader(url));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        List<PlayDTO> plays = objectMapper.readValue(jsonContent, typeFactory.constructCollectionType(List.class, PlayDTO.class));
        assertThat(plays.size()).isEqualTo(403);
    }

    @Test
    public void shouldLocateThroughClassPath() throws IOException {
        String url = baseDataLoader.getUrl("my_movies.json").get();
        File file = new File(url);
        String jsonContent = FileCopyUtils.copyToString(new FileReader(file));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        MyMovies myMovies = objectMapper.readValue(jsonContent, MyMovies.class);
        assertThat(myMovies.getWanted().size()).isEqualTo(59);

    }
}