package evolution.mutation;

import app.Configuration;
import evolution.Individuum;

public abstract class MutationStrategy {
    // TODO. mutationRate is not used yet. It should probably be implemented in this abstract class, cause the behaviour is equal for all implementations.
    protected final double mutationRate;

    protected MutationStrategy(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    abstract Individuum mutate(Individuum i);


    // TODO: register implementations
    public static MutationStrategy get(MutationType mutationType){
        double mutationRate = Configuration.INSTANCE.mutationRate;

        return switch (mutationType){
            case CIM -> new CIM(mutationRate, Configuration.INSTANCE.randomGenerator);
            case SCM -> throw new RuntimeException("SCM is not implemented yet.");

            default -> throw new RuntimeException(mutationType+" is not implemented yet.");
        };
    }
}
