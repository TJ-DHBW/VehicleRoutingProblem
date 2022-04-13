package evolution.mutation;

import evolution.IGene;
import evolution.Individuum;

import java.util.ArrayList;
import java.util.Random;

public class CIM extends MutationStrategy{
    private final Random randomGenerator;

    protected CIM(double mutationRate, Random randomGenerator) {
        super(mutationRate);
        this.randomGenerator = randomGenerator;
    }

    // TODO Test this implementation
    @Override
    Individuum mutate(Individuum individuum) {
        ArrayList<IGene> genes = individuum.getGenotype().getGenes();
        int geneCount = genes.size();

        int inversionPoint = randomGenerator.nextInt(geneCount);

        ArrayList<IGene> genesOriginalOrder = new ArrayList<>(genes);

        for (int i = 0; i <= inversionPoint; i++) {
            genes.set(i, genesOriginalOrder.get(inversionPoint-i));
        }
        double secondRange = geneCount - inversionPoint;
        for (int i = 1; i < secondRange; i++) {
            genes.set(inversionPoint + i, genesOriginalOrder.get(geneCount - i));
        }

        return individuum;
    }
}
