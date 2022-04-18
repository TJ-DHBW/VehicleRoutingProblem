import evolution.Individuum;
import evolution.mutation.CIM;
import evolution.mutation.MutationStrategy;
import org.junit.jupiter.api.*;
import random.MersenneTwisterFast;
import vrp.Customer;
import vrp.Route;

import java.util.*;

import static org.mockito.Mockito.mock;

public class TestMutation {
    private MutationStrategy mutationStrategy;
    private Individuum<Route, Customer> testIndividuum;
    private Customer[] customers;

    @BeforeEach
    public void beforeEach(){
        Random r = mock(MersenneTwisterFast.class);
        // TODO: Random always returns 0 right now
        // TODO: Make this dynamic to the Config or always test all implementations.
        this.mutationStrategy = new CIM(r);

        customers = new Customer[]{
                new Customer(1, 0, 0, 0, 0, 0, 0),
                new Customer(2, 0, 0, 0, 0, 0, 0),
                new Customer(3, 0, 0, 0, 0, 0, 0),
                new Customer(4, 0, 0, 0, 0, 0, 0),
        };
        ArrayList<Customer> route = new ArrayList<>(List.of(this.customers));
        this.testIndividuum = new Individuum<>(new Route(route), ignore -> 1.0);
    }

    @Test
    @Order(1)
    @DisplayName("mutation should not change tour length")
    public void length() {
        int initialLength = testIndividuum.getGenotype().getGenes().size();

        this.mutationStrategy.mutate(this.testIndividuum);

        Assertions.assertEquals(initialLength, testIndividuum.getGenotype().getGenes().size());
    }

    @Test
    @Order(2)
    @DisplayName("no duplicate customers should occur in children")
    public void duplicates() {
        this.mutationStrategy.mutate(this.testIndividuum);

        HashSet<Customer> seen = new HashSet<>();
        for (Customer customer : this.testIndividuum.getGenotype().getGenes()){
            if (seen.contains(customer)) {
                Assertions.fail("Found duplicate: "+customer);
            }
            seen.add(customer);
        }
    }

    @Test
    @Order(3)
    @DisplayName("all customers should be included")
    public void completeness() {
        this.mutationStrategy.mutate(this.testIndividuum);

        for (Customer customer : this.customers) {
            if (!this.testIndividuum.getGenotype().getGenes().contains(customer)) {
                Assertions.fail();
            }
        }
    }

    @Test
    @Order(4)
    @DisplayName("permutation of the mutated individuum should be different from before the mutation")
    public void structure() {
        ArrayList<Customer> originalPermutation = new ArrayList<>(this.testIndividuum.getGenotype().getGenes());

        this.mutationStrategy.mutate(this.testIndividuum);

        for (int i = 0; i < this.testIndividuum.getGenotype().getGenes().size(); i++) {
            if (!this.testIndividuum.getGenotype().getGenes().get(i).equals(originalPermutation.get(i))) {
                return;
            }
        }
        Assertions.fail();
    }
}
