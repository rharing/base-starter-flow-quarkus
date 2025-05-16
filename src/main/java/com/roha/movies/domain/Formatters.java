package com.roha.movies.domain;

import java.time.format.DateTimeFormatter;

public class Formatters {
    public static DateTimeFormatter TIMEFORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static DateTimeFormatter PLAY_ID_FORMATTER = DateTimeFormatter.ofPattern("MM-dd_HH:mm");
    public static DateTimeFormatter DATETIMEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
}