import evolution.Individuum;
import evolution.crossover.CrossoverStrategy;
import evolution.crossover.UPX;
import org.junit.jupiter.api.*;
import vrp.Customer;
import vrp.Route;

import java.util.ArrayList;
import java.util.Random;

import static org.mockito.Mockito.mock;

// TODO: Implement the remaining tests!
public class TestCrossover {
    private CrossoverStrategy crossoverStrategy;
    private Individuum<Route, Customer> testIndividuum1;
    private Individuum<Route, Customer> testIndividuum2;

    @BeforeEach
    public void beforeEach(){
        Random r = mock(Random.class);
        // TODO: Make this dynamic to the Config or always test all implementations.
        this.crossoverStrategy = new UPX(r, 0.5);

        // TODO: create the testIndividuums.
    }

    @Test
    @Order(1)
    @DisplayName("children should have same tour length as the parents")
    public void length() {
        int tourSizeParent1 = this.testIndividuum1.getGenotype().getGenes().size();
        int tourSizeParent2 = this.testIndividuum2.getGenotype().getGenes().size();
        Assertions.assertEquals(tourSizeParent1, tourSizeParent2);

        ArrayList<Individuum<?, ?>> children = this.crossoverStrategy.execute(this.testIndividuum1, this.testIndividuum2);

        int tourSizeChild1 = children.get(0).getGenotype().getGenes().size();
        int tourSizeChild2 = children.get(1).getGenotype().getGenes().size();
        Assertions.assertEquals(tourSizeChild1, tourSizeParent1);
        Assertions.assertEquals(tourSizeChild2, tourSizeParent1);
    }

    @Test
    @Order(2)
    @DisplayName("no duplicate tours should occur in children")
    public void duplicates() {

    }

    @Test
    @Order(3)
    @DisplayName("all tour indices should be included for each child")
    public void completeness() {

    }

    @Test
    @Order(4)
    @DisplayName("permutation of each child should be different from the permutations of the parents")
    public void structure() {

    }
}
