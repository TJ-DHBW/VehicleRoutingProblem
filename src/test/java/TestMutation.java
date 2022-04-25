import evolution.Individuum;
import evolution.mutation.CIM;
import evolution.mutation.MutationStrategy;
import evolution.mutation.SCM;
import org.junit.jupiter.api.*;
import random.MersenneTwisterFast;
import vrp.Customer;
import vrp.Route;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestMutation {
    private MutationStrategy[] mutationStrategies;
    private Individuum<Route, Customer> templateIndividuum;
    private Customer[] customers;

    @BeforeEach
    public void beforeEach(){
        Random randomCIM = mock(MersenneTwisterFast.class);
        when(randomCIM.nextInt(anyInt())).thenReturn(0);
        Random randomSCM = mock(MersenneTwisterFast.class);
        when(randomSCM.nextInt(anyInt())).thenReturn(1,0);
        this.mutationStrategies = new MutationStrategy[]{
                new CIM(randomCIM),
                new SCM(randomSCM)
        };

        customers = new Customer[]{
                new Customer(1, 0, 0, 0, 0, 0, 0),
                new Customer(2, 0, 0, 0, 0, 0, 0),
                new Customer(3, 0, 0, 0, 0, 0, 0),
                new Customer(4, 0, 0, 0, 0, 0, 0),
        };
        ArrayList<Customer> route = new ArrayList<>(List.of(this.customers));
        this.templateIndividuum = new Individuum<>(new Route(route), ignore -> 1.0);
    }

    @Test
    @Order(1)
    @DisplayName("mutation should not change tour length")
    public void length() {
        for (MutationStrategy mutationStrategy : this.mutationStrategies) {
            System.out.println("Testing mutationStrategy: "+mutationStrategy.getClass().getCanonicalName());

            Individuum<Route, Customer> testIndividuum = this.templateIndividuum.createCopy();
            int initialLength = testIndividuum.getGenotype().getGenes().size();

            mutationStrategy.mutate(testIndividuum);

            Assertions.assertEquals(initialLength, testIndividuum.getGenotype().getGenes().size());

            System.out.println("Success: "+mutationStrategy.getClass().getCanonicalName());
        }
    }

    @Test
    @Order(2)
    @DisplayName("no duplicate customers should occur in mutated individuals")
    public void duplicates() {
        for (MutationStrategy mutationStrategy : this.mutationStrategies) {
            System.out.println("Testing mutationStrategy: "+mutationStrategy.getClass().getCanonicalName());

            Individuum<Route, Customer> testIndividuum = this.templateIndividuum.createCopy();

            mutationStrategy.mutate(testIndividuum);

            HashSet<Customer> seen = new HashSet<>();
            for (Customer customer : testIndividuum.getGenotype().getGenes()){
                if (seen.contains(customer)) {
                    Assertions.fail("Found duplicate: "+customer);
                }
                seen.add(customer);
            }

            System.out.println("Success: "+mutationStrategy.getClass().getCanonicalName());
        }
    }

    @Test
    @Order(3)
    @DisplayName("all customers should be included")
    public void completeness() {
        for (MutationStrategy mutationStrategy : this.mutationStrategies) {
            System.out.println("Testing mutationStrategy: "+mutationStrategy.getClass().getCanonicalName());

            Individuum<Route, Customer> testIndividuum = this.templateIndividuum.createCopy();

            mutationStrategy.mutate(testIndividuum);

            for (Customer customer : this.customers) {
                if (!testIndividuum.getGenotype().getGenes().contains(customer)) {
                    Assertions.fail();
                }
            }

            System.out.println("Success: "+mutationStrategy.getClass().getCanonicalName());
        }
    }

    @Test
    @Order(4)
    @DisplayName("permutation of the mutated individuum should be different from before the mutation")
    public void structure() {
        for (MutationStrategy mutationStrategy : this.mutationStrategies) {
            System.out.println("Testing mutationStrategy: "+mutationStrategy.getClass().getCanonicalName());

            Individuum<Route, Customer> testIndividuum = this.templateIndividuum.createCopy();

            mutationStrategy.mutate(testIndividuum);

            Assertions.assertNotEquals(this.templateIndividuum.getGenotype(), testIndividuum.getGenotype());

            System.out.println("Success: "+mutationStrategy.getClass().getCanonicalName());
        }
    }
}
