package com.roha.movies.fetcher;

import org.jsoup.nodes.Document;

import java.io.IOException;

public sealed interface DocumentLoader permits DocumentLoaderFromFile, ExternalDocumentLoader {

    Document parse() throws IOException;

    static DocumentLoader create(String what){
    if(what.startsWith("file:/")){
        return new DocumentLoaderFromFile(what);
    }
    else return new ExternalDocumentLoader(what);

    }
}
