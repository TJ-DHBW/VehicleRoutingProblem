package evolution.crossover;

import evolution.Individuum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class UPX extends CrossoverStrategy {
    private final Random randomGenerator;

    protected UPX(double crossoverRate, Random randomGenerator) {
        super(crossoverRate);
        this.randomGenerator = randomGenerator;
    }

    // This implementation is taken straight from the Crossover Tutorial and suffers from the same problems explained there.
    // TODO: Rework this implementation!
    @Override
    ArrayList<Individuum> execute(Individuum parent1, Individuum parent2) {
        ArrayList<Individuum> children = new ArrayList<>();

        /*
        Individuum child1 = parent01.copy();
        Individuum child2 = parent02.copy();

        HashMap<String, Integer> p1 = new HashMap<>();
        for (int i = 0; i < child1.tourSize(); i++) {
            p1.put(child1.getCity(i).toString(), i);
        }
        HashMap<String, Integer> p2 = new HashMap<>();
        for (int i = 0; i < child2.tourSize(); i++) {
            p2.put(child2.getCity(i).toString(), i);
        }

        // Unused in the current implementation
        int tourSize = parent01.tourSize();
        int a = this.randomGenerator.nextInt(tourSize);
        int b = this.randomGenerator.nextInt(a, tourSize);

        boolean foundEquals;
        int retryCount = 0;
        do {
            for (int i = 0; i < tourSize; i++) {
                double q = this.randomGenerator.nextDouble();
                // Commented lines are exactly as the algorithm specifies, the line above is our understanding.
                if (q >= this.p) {
                    City t1 = child1.getCity(i);
                    String t1s = t1.toString();
                    City t2 = child2.getCity(i);
                    String t2s = t2.toString();

                    child1.setCity(i, t2);
                    child1.setCity(p1.get(t2s), t1);
                    //child1.setCity(p1.get(t1s), t1);

                    child2.setCity(i, t1);
                    child2.setCity(p2.get(t1s), t2);
                    //child2.setCity(p2.get(t2s), t2);

                    int tmp = p1.get(t1s);
                    p1.put(t1s, p1.get(t2s));
                    p1.put(t2s, tmp);
                    //p1.put(t2s, p1.get(t1s));

                    tmp = p2.get(t1s);
                    p2.put(t1s, p2.get(t2s));
                    p2.put(t2s, tmp);
                    //p2.put(t2s, p2.get(t1s));
                }
            }

            // Needed, because of the altered implementation of UPX
            foundEquals = child1.equals(parent01)
                    || child1.equals(parent02)
                    || child1.equals(child2)
                    || child2.equals(parent01)
                    || child2.equals(parent02);
            retryCount++;
            if (retryCount > 20)
                throw new RuntimeException("Was not able to create differing children after 20 tries.");
        } while (foundEquals);

        children.add(child1);
        children.add(child2);*/
        return children;
    }
}
