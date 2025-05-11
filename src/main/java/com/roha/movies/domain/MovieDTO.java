package com.roha.movies.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class MovieDTO {

    String id;
    @JsonProperty("movie-id")
    String movieId;
    String title;
    String titleAddOn;
    String image;
    String href;
    String rating;
    Date createdAt;
    Integer duration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleAddOn() {
        return titleAddOn;
    }

    public void setTitleAddOn(String titleAddOn) {
        this.titleAddOn = titleAddOn;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MovieDTO movieDTO = (MovieDTO) o;

        return new EqualsBuilder().append(movieId, movieDTO.movieId).append(titleAddOn, movieDTO.titleAddOn).append(image, movieDTO.image).append(href, movieDTO.href).append(rating, movieDTO.rating).append(createdAt, movieDTO.createdAt).append(duration, movieDTO.duration).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(movieId).append(titleAddOn).append(image).append(href).append(rating).append(createdAt).append(duration).toHashCode();
    }
}
