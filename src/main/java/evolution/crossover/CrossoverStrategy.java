package evolution.crossover;

import app.Configuration;
import evolution.Individuum;

import java.util.ArrayList;

public abstract class CrossoverStrategy {
    private int crossoverCount = 0;
    public ArrayList<Individuum<?, ?>> execute(Individuum<?, ?> parent1, Individuum<?, ?> parent2){
        crossoverCount++;
        return executeInner(parent1, parent2);
    }

    protected abstract ArrayList<Individuum<?, ?>> executeInner(Individuum<?, ?> parent1, Individuum<?, ?> parent2);

    public int getCrossoverCount(){
        return crossoverCount;
    }

    // TODO: register implementations
    public static CrossoverStrategy get(CrossoverType crossoverType){
        return switch (crossoverType){
            case HRX -> throw new RuntimeException("HRX is not implemented yet.");
            case UPX -> new UPX(Configuration.INSTANCE.randomGenerator, 0.7);

            default -> throw new RuntimeException(crossoverType+" is not implemented yet.");
        };
    }
}
