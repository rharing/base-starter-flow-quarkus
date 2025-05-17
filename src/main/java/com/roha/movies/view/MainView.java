package com.roha.movies.view;

import com.roha.movies.GreetService;
import com.roha.movies.domain.City;
import com.roha.movies.fetcher.Initializer;
import com.roha.movies.fetcher.MoviesFetcher;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import jakarta.inject.Inject;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
public class MainView extends VerticalLayout {

    private Grid grid = new Grid<City>(City.class, false);

    private final Initializer initializer;

    public MainView(Initializer initializer) {
        this.initializer = initializer;
        // Use TextField for standard text input
        TextField textField = new TextField("Stad");
        textField.addThemeName("bordered");

        // Button click listeners can be defined as lambda expressions
        Button button = new Button("Zoeken", e -> {
            String city = textField.getValue().toLowerCase();
            List<City> cities = new ArrayList<>();
            try {
                MoviesFetcher moviesFetcher = initializer.getMoviesFetcher();
                cities = moviesFetcher.loadCities().stream().filter(city1 -> city1.name().toLowerCase().contains(city)).toList();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            grid.setItems(cities);
        });

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button is more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        if(initializer!= null) {
            try {
                MoviesFetcher moviesFetcher = initializer.getMoviesFetcher();
                List<City> cities = moviesFetcher.loadCities();
                grid.addColumn("name")
                        .setHeader("Stad").setTextAlign(ColumnTextAlign.END);
                grid.setItems(cities);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        add(textField, button, grid);

    }
}
