package evolution.mutation;

import app.Configuration;
import evolution.Individuum;

public abstract class MutationStrategy {
    public abstract void mutate(Individuum<?, ?> i);


    // TODO: register implementations
    public static MutationStrategy get(MutationType mutationType){
        return switch (mutationType){
            case CIM -> new CIM(Configuration.INSTANCE.randomGenerator);
            case SCM -> throw new RuntimeException("SCM is not implemented yet.");

            default -> throw new RuntimeException(mutationType+" is not implemented yet.");
        };
    }
}
