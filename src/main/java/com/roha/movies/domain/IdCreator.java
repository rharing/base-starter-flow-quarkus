package com.roha.movies.domain;

public class IdCreator {
    public static String create(String name) {
        return name.toLowerCase().replaceAll(" ", "-").replaceAll("\\.", "_").replaceAll(":", "_");
    }
}
