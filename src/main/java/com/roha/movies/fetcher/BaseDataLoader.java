package com.roha.movies.fetcher;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class BaseDataLoader {
    public Optional<String> getUrl(String location) throws IOException {
        Optional<Resource> resource = loadResource(location);
        if (resource.isPresent() && resource.get().exists()) {
            return Optional.of(resource.get().getURL().getPath());
        } else {
            return Optional.empty();
        }
    }

    private Optional<Resource> loadResource(String location) throws IOException {
        Resource resource = new ClassPathResource(location);
        if (resource.exists()) {
            return Optional.of(resource);
        } else {
            resource = new ClassPathResource(".");
            String base = resource.getURL().toExternalForm().split("target")[0];
            if (base.startsWith("file:/")) {
                base = base.substring(("file:/".length()));
            }
            String os = System.getProperty("os.name");
            if (!os.contains("indows")) {
                base = "/" + base;
            }
            String url = Path.of(base, "go", "resources", location).toString();
            FileSystemResource fileSystemResource = new FileSystemResource(url);
            if (fileSystemResource.exists()) {
                return Optional.of(fileSystemResource);
            }
            return Optional.empty();
        }
    }

    public Optional<String> getExternalUrl(String location) throws IOException {
        Optional<Resource> resource = loadResource(location);
        if (resource.isPresent()) {
            return Optional.of(resource.get().getURL().toExternalForm());
        }
        return Optional.empty();
    }
}
