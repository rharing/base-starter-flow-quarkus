package com.roha.movies.components;

import com.roha.movies.view.CityView;
import com.roha.movies.view.domain.PlayPerDay;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class PlayDiv extends Div {
    private final PlayPerDay playPerDay;
    private final CityView parent;

    public PlayDiv(PlayPerDay playPerDay, CityView parent) {
        this.playPerDay = playPerDay;
        this.parent = parent;
        VerticalLayout layout = new VerticalLayout();
        Div label = new Div(playPerDay.render());
        layout.add(label);
        this.add(layout);
    }

}
