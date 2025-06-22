package com.roha.movies.components;

import com.roha.movies.view.CityView;
import com.roha.movies.view.domain.DayOverview;
import com.roha.movies.view.domain.PlayPerDay;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DayOverviewDiv extends Div {
    private final DayOverview dayOverview;
    private final CityView parent;

    public DayOverviewDiv(DayOverview dayOverview, CityView parent) {
        this.dayOverview = dayOverview;
        this.parent = parent;
        VerticalLayout layout = new VerticalLayout();
        layout.add(new H3(dayOverview.getDag()));
        for (PlayPerDay playPerDay : dayOverview.getPlays()) {
            layout.add(new PlayDiv(playPerDay, parent));
        }
        this.add(layout);
    }
}
