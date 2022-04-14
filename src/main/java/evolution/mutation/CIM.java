package evolution.mutation;

import evolution.Individuum;

import java.util.ArrayList;
import java.util.Random;

public class CIM extends MutationStrategy{
    private final Random randomGenerator;

    protected CIM(Random randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    @Override
    void mutate(Individuum<?, ?> individuum) {
        mutateHelper(individuum);
    }

    // TODO Test this implementation
    private <T> void mutateHelper(Individuum<?, ?> individuum){
        @SuppressWarnings("unchecked")
        ArrayList<T> genes = (ArrayList<T>) individuum.getGenotype().getGenes();
        int geneCount = genes.size();

        int inversionPoint = randomGenerator.nextInt(geneCount);

        ArrayList<T> genesOriginalOrder = new ArrayList<>(genes);

        for (int i = 0; i <= inversionPoint; i++) {
            genes.set(i, genesOriginalOrder.get(inversionPoint-i));
        }
        double secondRange = geneCount - inversionPoint;
        for (int i = 1; i < secondRange; i++) {
            genes.set(inversionPoint + i, genesOriginalOrder.get(geneCount - i));
        }
    }
}