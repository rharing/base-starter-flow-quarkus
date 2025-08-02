package com.roha.movies.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.checkbox.Checkbox;

public class CinemaSelectedEvent extends ComponentEvent<Checkbox> {
    private final String cinema;
    private final Boolean selected;

    public CinemaSelectedEvent(final Checkbox source, final String cinema, final boolean fromClient) {
        super(source, fromClient);
        this.cinema = cinema;
        this.selected = source.getValue();
    }


    public String getCinema() {
        return cinema;
    }

    public Boolean getSelected() {
        return selected;
    }
}
