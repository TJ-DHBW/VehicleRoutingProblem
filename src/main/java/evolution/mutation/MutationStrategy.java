package evolution.mutation;

import app.Configuration;
import evolution.Individuum;

public abstract class MutationStrategy {
    private int mutationCount = 0;

    public void mutate(Individuum<?, ?> i){
        mutationCount++;
        mutateInner(i);
    }

    protected abstract void mutateInner(Individuum<?, ?> i);

    public int getMutationCount() {
        return mutationCount;
    }

    // TODO: register implementations
    public static MutationStrategy get(MutationType mutationType){
        return switch (mutationType){
            case CIM -> new CIM(Configuration.INSTANCE.randomGenerator);
            case SCM -> throw new RuntimeException("SCM is not implemented yet.");

            default -> throw new RuntimeException(mutationType+" is not implemented yet.");
        };
    }
}
