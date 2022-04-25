import app.DataInstance;
import app.DataManagement;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestSelection {
    private SelectionStrategy[] selectionStrategies;
    private ArrayList<Individuum<Route, Customer>> testPopulation;
    private int testK;

    @BeforeEach
    public void beforeEach() {
        Random randomTOS = mock(MersenneTwisterFast.class);
        when(randomTOS.nextInt(anyInt())).thenReturn(0);
        Random randomRWS = mock(MersenneTwisterFast.class);
        when(randomRWS.nextInt(anyInt())).thenReturn(0);
        when(randomRWS.nextDouble()).thenReturn(0.1, 0.5, 0.7, 0.3, 0.9, 0.98);
        this.selectionStrategies = new SelectionStrategy[]{
                new TOS(randomTOS)
        };

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
        DataInstance ukraineData = DataManagement.readData();
        Assertions.assertTrue(ukraineData.getDepots().size() > 0);
    }

    @Test
    @Order(2)
    @DisplayName("select an even number of individuals")
    public void evenNumber() {
        for (SelectionStrategy selectionStrategy : this.selectionStrategies) {
            System.out.println("Testing selectionStrategy: "+selectionStrategy.getClass().getCanonicalName());

            ArrayList<Individuum<Route, Customer>> selection = selectionStrategy.select(this.testPopulation, this.testK);

            Assertions.assertEquals(0, selection.size() % 2);

            System.out.println("Success: "+selectionStrategy.getClass().getCanonicalName());
        }
    }

    @Test
    @Order(3)
    @DisplayName("selection does not contain duplicates")
    public void noDuplicates() {
        for (SelectionStrategy selectionStrategy : this.selectionStrategies) {
            System.out.println("Testing selectionStrategy: "+selectionStrategy.getClass().getCanonicalName());

            ArrayList<Individuum<Route, Customer>> selection = selectionStrategy.select(this.testPopulation, this.testK);

            HashSet<Individuum<Route, Customer>> seen = new HashSet<>();
            for (Individuum<Route, Customer> individuum : selection) {
                if (seen.contains(individuum)) {
                    Assertions.fail();
                }
                seen.add(individuum);
            }

            System.out.println("Success: "+selectionStrategy.getClass().getCanonicalName());
        }
    }
}
