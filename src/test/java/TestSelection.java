import evolution.Individuum;
import evolution.selection.SelectionStrategy;
import evolution.selection.TOS;
import org.junit.jupiter.api.*;
import random.MersenneTwisterFast;
import vrp.Customer;
import vrp.Route;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static org.mockito.Mockito.mock;

public class TestSelection {
    private SelectionStrategy selectionStrategy;
    private ArrayList<Individuum<Route, Customer>> testPopulation;
    private int testK;

    @BeforeEach
    public void beforeEach() {
        Random r = mock(MersenneTwisterFast.class);
        // TODO: Random always returns 0 right now
        // TODO: Make this dynamic to the Config or always test all implementations.
        this.selectionStrategy = new TOS(r);

        Function<ArrayList<Customer>, Double> ff = ignore -> 1.0;
        Customer[] customers = new Customer[]{
                new Customer(1, 0, 0, 0, 0, 0, 0),
                new Customer(2, 0, 0, 0, 0, 0, 0),
                new Customer(3, 0, 0, 0, 0, 0, 0),
                new Customer(4, 0, 0, 0, 0, 0, 0),
        };
        Individuum<Route, Customer> i1 = new Individuum<>(new Route(List.of(customers[0], customers[1], customers[2], customers[3])), ff);
        Individuum<Route, Customer> i2 = new Individuum<>(new Route(List.of(customers[0], customers[1], customers[3], customers[2])), ff);
        Individuum<Route, Customer> i3 = new Individuum<>(new Route(List.of(customers[0], customers[2], customers[1], customers[3])), ff);
        Individuum<Route, Customer> i4 = new Individuum<>(new Route(List.of(customers[0], customers[2], customers[3], customers[1])), ff);
        Individuum<Route, Customer> i5 = new Individuum<>(new Route(List.of(customers[0], customers[3], customers[1], customers[2])), ff);
        Individuum<Route, Customer> i6 = new Individuum<>(new Route(List.of(customers[0], customers[3], customers[2], customers[1])), ff);
        Individuum<Route, Customer> i7 = new Individuum<>(new Route(List.of(customers[1], customers[0], customers[2], customers[3])), ff);
        this.testPopulation = new ArrayList<>(List.of(i1, i2, i3, i4, i5, i6, i7));
        this.testK = 3;
    }

    @Test
    @Order(1)
    @DisplayName("depot has been initialised completely")
    public void depotComplete() {
        // TODO: what the hell are we supposed to do here?
    }
    @Test
    @Order(2)
    @DisplayName("select an even number of individuals")
    public void evenNumber() {
        ArrayList<Individuum<Route, Customer>> selection = this.selectionStrategy.select(this.testPopulation, this.testK);

        Assertions.assertEquals(0, selection.size() % 2);
    }
    @Test
    @Order(3)
    @DisplayName("selection does not contain duplicates")
    public void noDuplicates() {
        ArrayList<Individuum<Route, Customer>> selection = this.selectionStrategy.select(this.testPopulation, this.testK);

        HashSet<Individuum<Route, Customer>> seen = new HashSet<>();
        for (Individuum<Route, Customer> individuum : selection) {
            if (seen.contains(individuum)) {
                Assertions.fail();
            }
            seen.add(individuum);
        }
    }
}
