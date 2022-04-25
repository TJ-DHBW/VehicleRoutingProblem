import evolution.Individuum;
import evolution.crossover.CrossoverStrategy;
import evolution.crossover.HRX;
import evolution.crossover.UPX;
import org.junit.jupiter.api.*;
import random.MersenneTwisterFast;
import vrp.Customer;
import vrp.Route;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestCrossover {
    private CrossoverStrategy[] crossoverStrategies;
    private Individuum<Route, Customer> testIndividuum1;
    private Individuum<Route, Customer> testIndividuum2;
    private Customer[] customers;

    @BeforeEach
    public void beforeEach(){
        Random randomUPX = mock(MersenneTwisterFast.class);
        when(randomUPX.nextDouble()).thenReturn(0.9, 0.0);
        Random randomHRX = mock(MersenneTwisterFast.class);
        when(randomHRX.nextInt()).thenReturn(1, 0);
        this.crossoverStrategies = new CrossoverStrategy[]{
                new UPX(randomUPX, 0.5),
                new HRX(randomHRX)
        };

        customers = new Customer[]{
                new Customer(1, 0, 0, 0, 0, 0, 0),
                new Customer(2, 0, 0, 0, 0, 0, 0),
                new Customer(3, 0, 0, 0, 0, 0, 0),
                new Customer(4, 0, 0, 0, 0, 0, 0),
        };
        ArrayList<Customer> route1 = new ArrayList<>(List.of(customers[0], customers[1], customers[2], customers[3]));
        ArrayList<Customer> route2 = new ArrayList<>(List.of(customers[3], customers[2], customers[1], customers[0]));
        this.testIndividuum1 = new Individuum<>(new Route(route1), ignore -> 1.0);
        this.testIndividuum2 = new Individuum<>(new Route(route2), ignore -> 1.0);
    }

    @Test
    @Order(1)
    @DisplayName("children should have same tour length as the parents")
    public void length() {
        int tourSizeParent1 = this.testIndividuum1.getGenotype().getGenes().size();
        int tourSizeParent2 = this.testIndividuum2.getGenotype().getGenes().size();
        Assertions.assertEquals(tourSizeParent1, tourSizeParent2);

        for (CrossoverStrategy crossoverStrategy : this.crossoverStrategies) {
            System.out.println("Testing crossoverStrategy: "+crossoverStrategy.getClass().getCanonicalName());

            ArrayList<Individuum<Route, Customer>> children = crossoverStrategy.execute(this.testIndividuum1, this.testIndividuum2);

            int tourSizeChild1 = children.get(0).getGenotype().getGenes().size();
            int tourSizeChild2 = children.get(1).getGenotype().getGenes().size();
            Assertions.assertEquals(tourSizeChild1, tourSizeParent1);
            Assertions.assertEquals(tourSizeChild2, tourSizeParent1);

            System.out.println("Success: "+crossoverStrategy.getClass().getCanonicalName());
        }
    }

    @Test
    @Order(2)
    @DisplayName("no duplicate tours should occur in children")
    public void duplicates() {
        for (CrossoverStrategy crossoverStrategy : this.crossoverStrategies) {
            System.out.println("Testing crossoverStrategy: "+crossoverStrategy.getClass().getCanonicalName());

            ArrayList<Individuum<Route, Customer>> children = crossoverStrategy.execute(this.testIndividuum1, this.testIndividuum2);

            Route child1Genotype = children.get(0).getGenotype();
            Route child2Genotype = children.get(1).getGenotype();

            Assertions.assertNotEquals(child1Genotype, child2Genotype);
            System.out.println("Success: "+crossoverStrategy.getClass().getCanonicalName());
        }
    }

    @Test
    @Order(3)
    @DisplayName("all customer indices should be included for each child")
    public void completeness() {
        for (CrossoverStrategy crossoverStrategy : this.crossoverStrategies) {
            System.out.println("Testing crossoverStrategy: "+crossoverStrategy.getClass().getCanonicalName());

            ArrayList<Individuum<Route, Customer>> children = crossoverStrategy.execute(this.testIndividuum1, this.testIndividuum2);

            ArrayList<Customer> child1Genes = children.get(0).getGenotype().getGenes();
            ArrayList<Customer> child2Genes = children.get(1).getGenotype().getGenes();

            for (Customer customer : this.customers) {
                Assertions.assertTrue(child1Genes.contains(customer));
                Assertions.assertTrue(child2Genes.contains(customer));
            }

            System.out.println("Success: "+crossoverStrategy.getClass().getCanonicalName());
        }
    }

    @Test
    @Order(4)
    @DisplayName("permutation of each child should be different from the permutations of the parents")
    public void structure() {
        for (CrossoverStrategy crossoverStrategy : this.crossoverStrategies) {
            System.out.println("Testing crossoverStrategy: "+crossoverStrategy.getClass().getCanonicalName());

            ArrayList<Individuum<Route, Customer>> children = crossoverStrategy.execute(this.testIndividuum1, this.testIndividuum2);

            Route child1Genotype = children.get(0).getGenotype();
            Route child2Genotype = children.get(1).getGenotype();

            Assertions.assertNotEquals(child1Genotype, this.testIndividuum1.getGenotype());
            Assertions.assertNotEquals(child1Genotype, this.testIndividuum2.getGenotype());

            Assertions.assertNotEquals(child2Genotype, this.testIndividuum1.getGenotype());
            Assertions.assertNotEquals(child2Genotype, this.testIndividuum2.getGenotype());

            System.out.println("Success: "+crossoverStrategy.getClass().getCanonicalName());
        }
    }
}
