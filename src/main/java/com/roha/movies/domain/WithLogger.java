package com.roha.movies.domain;

import java.util.logging.Logger;

public interface WithLogger {
    default Logger logger() {
        return Logger.getLogger(getClass().getName());
    }
}