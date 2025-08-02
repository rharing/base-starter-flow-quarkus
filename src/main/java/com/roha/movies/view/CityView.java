package com.roha.movies.view;

import com.roha.movies.components.MovieWithPlaysDiv;
import com.roha.movies.components.ReloadMoviesEvent;
import com.roha.movies.fetcher.Initializer;
import com.roha.movies.view.domain.CityOverView;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import java.io.IOException;

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
            header.add(new H3("Films in " + this.city));
            add(header);
            VerticalLayout movies = new VerticalLayout();
            CityOverView cityOverView = new CityOverView(initializer, this.city);
            cityOverView.getMovies().forEach(movie -> movies.add(new MovieWithPlaysDiv(initializer, movie, null)));
            add(header, movies);
            ComponentUtil.addListener(this, ReloadMoviesEvent.class, event -> {
                        try {
                            initializer.updateMyMovie(event.getMovie().asDTO(), event.getMyMoviesAction());
                            Notification notification = new Notification("handled " + event.getMyMoviesAction(), 3, Notification.Position.MIDDLE);
                            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                            notification.open();
                        } catch (IOException e) {
                            Notification notification = new Notification("oeps " + e.getMessage(), 3, Notification.Position.MIDDLE);
                            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                            notification.open();
                        }
                    }
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
