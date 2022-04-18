package evolution.mutation;

import evolution.IGene;
import evolution.IGenotype;
import evolution.Individuum;

import java.util.ArrayList;
import java.util.Random;

public class CIM extends MutationStrategy{
    private final Random randomGenerator;

    public CIM(Random randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    @Override
    public <T extends IGenotype<U>, U extends IGene> void mutateInner(Individuum<T, U> individuum) {
        ArrayList<U> genes = individuum.getGenotype().getGenes();
        int geneCount = genes.size();

        int inversionPoint = randomGenerator.nextInt(geneCount);

        ArrayList<U> genesOriginalOrder = new ArrayList<>(genes);

        for (int i = 0; i <= inversionPoint; i++) {
            genes.set(i, genesOriginalOrder.get(inversionPoint-i));
        }
        double secondRange = geneCount - inversionPoint;
        for (int i = 1; i < secondRange; i++) {
            genes.set(inversionPoint + i, genesOriginalOrder.get(geneCount - i));
        }
    }
}
