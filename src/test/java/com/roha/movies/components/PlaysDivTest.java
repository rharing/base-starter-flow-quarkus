package com.roha.movies.components;

import com.vaadin.testbench.unit.ComponentTester;
import com.vaadin.testbench.unit.Tests;

import static org.junit.jupiter.api.Assertions.*;

@Tests(PlaysDiv.class)
class PlaysDivTest  extends ComponentTester<PlaysDiv> {

    public PlaysDivTest(PlaysDiv component) {
        super(component);
    }
}