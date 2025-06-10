package com.roha.movies.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Layout;

@Layout
public class MainLayout extends AppLayout {
    public MainLayout() {
        Div title = new Div("Ik wil nu naar de film");
        addToNavbar(title);
    }
}
