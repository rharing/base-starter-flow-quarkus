package com.roha.movies.fetcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roha.movies.domain.MyMovies;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LocalMyMoviesRepository implements MyMoviesRepository {
    private final String backendFile;
    private final AbstractResource storage;

    public LocalMyMoviesRepository() {
        this("my_movies.json");
    }

    public LocalMyMoviesRepository(String backendFile) {
        this.backendFile = backendFile;
        if(backendFile != null && backendFile.startsWith("file:")){
            this.storage= new FileSystemResource(backendFile.substring(5));
        }
        else {
            this.storage = new ClassPathResource(backendFile);
        }
        try {
            File storageFile = this.storage.getFile();
            String absolutePath = storageFile.getAbsolutePath();
        } catch (IOException e) {
//            log.info("could not get the file, yet the storage was found", e);
        }
    }

    @Override
    public MyMovies load() {
        if (storage.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                File file = storage.getFile();
                return mapper.readValue(FileUtils.readFileToString(file, StandardCharsets.UTF_8), MyMovies.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new MyMovies();
    }

    @Override
    public void save(MyMovies myMovies) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonContent = objectMapper.writeValueAsString(myMovies);
            File storage = null;
            try {
                storage = this.storage.getFile();
            } catch (IOException e) {
                String root = this.getClass().getClassLoader().getResource("").getPath();
                storage = new File(root, backendFile);
            }
            FileCopyUtils.copy(jsonContent, new FileWriter(storage));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clean() {
        String root = this.getClass().getClassLoader().getResource("").getPath();
        File storage = new File(root, backendFile);
        storage.delete();

    }
}
