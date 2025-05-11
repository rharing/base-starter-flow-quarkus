package com.roha.movies.fetcher;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public final class DocumentLoaderFromFile implements DocumentLoader {
    private String root;

    public DocumentLoaderFromFile(String root) {
        if (root != null) {
            File file = new File(root);
            if (root.startsWith("file:")) {
                file = new File(root.substring(5));
            }
            if (file.exists()) {
                this.root = file.getAbsolutePath();
            }
        }
    }

    public void load(String root) {
        if (root == null) {
            BaseDataLoader baseDataLoader = new BaseDataLoader();
            try {
                Optional<String> optionalUrl = baseDataLoader.getUrl("overview_haarlem.html");
                if(optionalUrl.isPresent()) {
                    this.root = optionalUrl.get();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.root = root;
        }
    }

    @Override
    public Document parse() throws IOException {
        if (root == null) {
            load(root);
            return parse();
        } else {
            File file = new File(root);
            if (this.root.startsWith("file:")) {
                file = new File(this.root.substring(5));
            }
            if (file.exists()) {
                String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                return Jsoup.parse(content);
            } else {
                throw new FileNotFoundException(String.format("File %s not found", root));
            }
        }
    }
}
