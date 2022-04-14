package evolution.crossover;

import evolution.Individuum;

import java.util.ArrayList;

public abstract class CrossoverStrategy {
    public abstract ArrayList<Individuum<?, ?>> execute(Individuum<?, ?> parent1, Individuum<?, ?> parent2);

    // TODO: register implementations
    public static CrossoverStrategy get(CrossoverType crossoverType){
        switch (crossoverType){
            case HRX -> throw new RuntimeException("HRX is not implemented yet.");
            case UPX -> throw new RuntimeException("UPX is not implemented yet.");

            default -> throw new RuntimeException(crossoverType+" is not implemented yet.");
        }
    }
}
