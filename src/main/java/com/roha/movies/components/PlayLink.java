package com.roha.movies.components;

import com.roha.movies.view.domain.PlayPerDay;
import com.vaadin.copilot.customcomponent.CustomComponent;
import com.vaadin.flow.component.html.Anchor;

public class PlayLink extends CustomComponentInPro {
    private Anchor anchor;

    @Override
    public Type getType() {
        return Type.IN_PROJECT;
    }

    @Override
    public Class<?> componentClass() {
        return PlayLink.class;
    }

    public PlayLink(final PlayPerDay playPerDay) {
        this.anchor = new Anchor(playPerDay.ticket(), playPerDay.toString());
    }

    public void setVisible(boolean visible){
        anchor.setVisible(visible);
    }
}
