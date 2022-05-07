package evolution.crossover;

import app.Configuration;
import evolution.IGene;
import evolution.IGenotype;
import evolution.Individuum;

import java.util.ArrayList;

public abstract class CrossoverStrategy {
    private int crossoverCount = 0;

    public <T extends IGenotype<U>, U extends IGene> ArrayList<Individuum<T, U>> execute(Individuum<T, U> parent1, Individuum<T, U> parent2) {
        crossoverCount++;
        return executeInner(parent1, parent2);
    }

    protected abstract <T extends IGenotype<U>, U extends IGene> ArrayList<Individuum<T, U>> executeInner(Individuum<T, U> parent1, Individuum<T, U> parent2);

    public int getCrossoverCount() {
        return crossoverCount;
    }

    public static CrossoverStrategy get(CrossoverType crossoverType) {
        return switch (crossoverType) {
            case HRX -> new HRX(Configuration.INSTANCE.randomGenerator);
            case UPX -> new UPX(Configuration.INSTANCE.randomGenerator, 0.7);

            default -> throw new RuntimeException(crossoverType + " is not implemented yet.");
        };
    }
}
