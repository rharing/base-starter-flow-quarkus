package com.roha.movies.fetcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

public final class ExternalDocumentLoader implements DocumentLoader {
    private final String root;

    public ExternalDocumentLoader(String root) {
        this.root = root;
    }
    //@todo add retry and such
    @Override
    public Document parse() throws IOException {
        if(this.root.startsWith("http://") || this.root.startsWith("https://")) {
            return Jsoup.connect(root).get();
        }
        else{
            File in = new File(this.root.substring(6));
            return Jsoup.parse(in, null);
        }
    }
}
