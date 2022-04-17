import evolution.Individuum;
import evolution.mutation.CIM;
import evolution.mutation.MutationStrategy;
import org.junit.jupiter.api.*;
import vrp.Customer;
import vrp.Route;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import static org.mockito.Mockito.mock;

public class TestMutation {
    private MutationStrategy mutationStrategy;
    private Individuum<Route, Customer> testIndividuum;

    @BeforeEach
    public void beforeEach(){
        Random r = mock(Random.class);
        // TODO: Make this dynamic to the Config or always test all implementations.
        this.mutationStrategy = new CIM(r);

        // TODO: create the testIndividuum.
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
        // TODO: Implement this test
    }

    @Test
    @Order(4)
    @DisplayName("permutation of the mutated individuum should be different from before the mutation")
    public void structure() {
        ArrayList<Customer> originalPermutation = this.testIndividuum.getGenotype().getGenes();

        this.mutationStrategy.mutate(this.testIndividuum);

        for (int i = 0; i < this.testIndividuum.getGenotype().getGenes().size(); i++) {
            if (!this.testIndividuum.getGenotype().getGenes().get(i).equals(originalPermutation.get(i))) {
                return;
            }
        }
        Assertions.fail();
    }
}
