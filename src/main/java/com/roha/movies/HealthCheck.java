package com.roha.movies;

import com.roha.movies.domain.MyMovies;
import com.roha.movies.fetcher.Initializer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.jobrunr.utils.CollectionUtils;

@Path("/health")
@ApplicationScoped
public class HealthCheck {

    private final Initializer initializer;

    public HealthCheck(Initializer initializer) {
        this.initializer = initializer;
    }

    @GET
    public String health() {
        MyMovies myMovies = initializer.getMyMovies();
        if(myMovies == null && CollectionUtils.isNotNullOrEmpty(myMovies.getSeen().keySet())) {
            return "No my movies found";
        }
        return "UP";
    }
}