package evolution.crossover;

import evolution.IGene;
import evolution.IGenotype;
import evolution.Individuum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class UPX extends CrossoverStrategy {
    private final Random randomGenerator;
    private final double p;

    public UPX(Random randomGenerator, double p) {
        this.randomGenerator = randomGenerator;
        this.p = p;
    }

    // This implementation is taken straight from the Crossover Tutorial and suffers from the same problems explained there.
    @Override
    protected <T extends IGenotype<U>, U extends IGene> ArrayList<Individuum<T, U>> executeInner(Individuum<T, U> parent1, Individuum<T, U> parent2) {
        if (parent1.getGenotype().getGenes().get(0).getClass() != parent2.getGenotype().getGenes().get(0).getClass()) throw new RuntimeException("Crossover only works with parents that hold the same concrete type of gene!");

        ArrayList<Individuum<T, U>> children = new ArrayList<>();

        Individuum<T, U> child1 = parent1.createCopy();
        Individuum<T, U> child2 = parent2.createCopy();

        ArrayList<U> child1Genes = child1.getGenotype().getGenes();
        ArrayList<U> child2Genes = child2.getGenotype().getGenes();

        HashMap<U, Integer> p1 = new HashMap<>();
        for (int i = 0; i < child1Genes.size(); i++) {
            p1.put(child1Genes.get(i), i);
        }
        HashMap<U, Integer> p2 = new HashMap<>();
        for (int i = 0; i < child2Genes.size(); i++) {
            p2.put(child2Genes.get(i), i);
        }

        int tourSize = parent1.getGenotype().getGenes().size();
        int a = this.randomGenerator.nextInt(tourSize);
        int b = this.randomGenerator.nextInt(a, tourSize);

        boolean foundEquals;
        int retryCount = 0;
        do {
            for (int i = 0; i < tourSize; i++) {
                double q = this.randomGenerator.nextDouble();
                if (q >= this.p) {
                    U t1 = child1Genes.get(i);
                    U t2 = child2Genes.get(i);

                    child1Genes.set(i, t2);
                    child1Genes.set(p1.get(t2), t1);

                    child2Genes.set(i, t1);
                    child2Genes.set(p2.get(t1), t2);

                    int tmp = p1.get(t1);
                    p1.put(t1, p1.get(t2));
                    p1.put(t2, tmp);

                    tmp = p2.get(t1);
                    p2.put(t1, p2.get(t2));
                    p2.put(t2, tmp);
                }
            }

            foundEquals = child1.equals(parent1)
                    || child1.equals(parent2)
                    || child1.equals(child2)
                    || child2.equals(parent1)
                    || child2.equals(parent2);
            retryCount++;
            if (retryCount > 20)
                throw new RuntimeException("Was not able to create differing children after 20 tries.");
        } while (foundEquals);

        children.add(child1);
        children.add(child2);

        return children;
    }
}
