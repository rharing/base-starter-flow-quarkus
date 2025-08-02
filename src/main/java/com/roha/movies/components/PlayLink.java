package com.roha.movies.components;

import com.roha.movies.view.domain.PlayPerDay;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class PlayLink extends HorizontalLayout {
    private Anchor anchor;
    private Icon icon;

    public PlayLink(final PlayPerDay playPerDay) {
        this.anchor = new Anchor(playPerDay.ticket(), playPerDay.toString());
        this.icon = VaadinIcon.CALENDAR.create();
        add(this.anchor);
        add(this.icon);
    }

    public void setVisible(boolean visible){

        anchor.setVisible(visible);
        icon.setVisible(visible);
    }
}
