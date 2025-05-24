package com.roha.movies.view;

import com.roha.movies.components.MovieWithPlaysDiv;
import com.roha.movies.domain.PlayDTO;
import com.roha.movies.fetcher.Initializer;
import com.roha.movies.view.domain.CityOverView;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.util.List;

@Route("city")
public class CityView extends VerticalLayout implements HasUrlParameter<String> {
    String city;
    private final Initializer initializer;

    public CityView(Initializer initializer) {
        this.initializer = initializer;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        this.city = s;
        try {
            HorizontalLayout header = new HorizontalLayout();
            header.add(new H3("Movies for" + this.city));
            add(header);
            VerticalLayout movies = new VerticalLayout();
            CityOverView cityOverView = new CityOverView(initializer, this.city);
            cityOverView.getMovies().forEach(movie -> movies.add(new MovieWithPlaysDiv(initializer,movie)));
            add(header, movies);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
