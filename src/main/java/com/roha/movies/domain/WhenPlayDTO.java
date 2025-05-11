package com.roha.movies.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record WhenPlayDTO(String city, @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start, @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end, String tickethref, String cinema) {
}
