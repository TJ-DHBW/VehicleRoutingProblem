package evolution.crossover;

import evolution.IGene;
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
    // TODO: Test this implementation!
    @Override
    protected ArrayList<Individuum<?, ?>> executeInner(Individuum<?, ?> parent1, Individuum<?, ?> parent2) {
        return executeHelper(parent1, parent2);
    }

    private <T extends IGene> ArrayList<Individuum<?, ?>> executeHelper(Individuum<?, ?> parent1, Individuum<?, ?> parent2){
        if (parent1.getGenotype().getGenes().get(0).getClass() != parent2.getGenotype().getGenes().get(0).getClass()) throw new RuntimeException("Crossover only works with parents that hold the same concrete type of gene!");

        ArrayList<Individuum<?, ?>> children = new ArrayList<>();

        Individuum<?, ?> child1 = parent1.createCopy();
        Individuum<?, ?> child2 = parent2.createCopy();

        @SuppressWarnings("unchecked")
        ArrayList<T> child1Genes = (ArrayList<T>) child1.getGenotype().getGenes();
        @SuppressWarnings("unchecked")
        ArrayList<T> child2Genes = (ArrayList<T>) child2.getGenotype().getGenes();

        HashMap<T, Integer> p1 = new HashMap<>();
        for (int i = 0; i < child1Genes.size(); i++) {
            p1.put(child1Genes.get(i), i);
        }
        HashMap<T, Integer> p2 = new HashMap<>();
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
                    T t1 = child1Genes.get(i);
                    T t2 = child2Genes.get(i);

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
