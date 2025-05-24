package com.roha.movies.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDateTime;

public record PlayDTO(String id, @JsonProperty("movie") MovieDTO movieDTO, @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime End, String tickethref, String cinema, String titleAddOn) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PlayDTO playDTO = (PlayDTO) o;

        return new EqualsBuilder().append(movieDTO, playDTO.movieDTO).append(End, playDTO.End).append(tickethref, playDTO.tickethref).append(titleAddOn, playDTO.titleAddOn).append(start, playDTO.start).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(movieDTO).append(start).append(End).append(tickethref).append(titleAddOn).toHashCode();
    }

    public Play withMovie(Movie movie) {
        return new Play(id(), movie, start, End, tickethref, cinema, titleAddOn);
    }
}
