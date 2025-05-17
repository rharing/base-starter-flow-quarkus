package com.roha.movies.fetcher;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class DocumentLoaderTest {

    @Test
    public void shouldLoadFileLoader() throws IOException {
        BaseDataLoader baseDataLoader = new BaseDataLoader();
        String filename = baseDataLoader.getExternalUrl ("overview_haarlem.html").get();
        DocumentLoader documentLoader = DocumentLoader.create(filename);
        MoviesDocumentParser moviesDocumentParser = new MoviesDocumentParser(documentLoader);
    }

}