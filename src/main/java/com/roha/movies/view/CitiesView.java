package com.roha.movies.view;

import com.roha.movies.GreetService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.inject.Inject;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
public class CitiesView extends VerticalLayout {

    @Inject
    GreetService greetService;

    public CitiesView() {
        // Use TextField for standard text input
        TextField textField = new TextField("Stad");
        textField.addThemeName("bordered");

        // Button click listeners can be defined as lambda expressions
        Button button = new Button("Zoeken", e -> {
            add(new Paragraph(greetService.greet(textField.getValue())));
        });

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button is more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        add(textField, button);
    }
}
