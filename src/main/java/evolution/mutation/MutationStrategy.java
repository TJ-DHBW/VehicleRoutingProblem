package evolution.mutation;

import app.Configuration;
import evolution.IGene;
import evolution.IGenotype;
import evolution.Individuum;

public abstract class MutationStrategy {
    private int mutationCount = 0;

    public <T extends IGenotype<U>, U extends IGene> void mutate(Individuum<T, U> i) {
        mutationCount++;
        mutateInner(i);
    }

    protected abstract <T extends IGenotype<U>, U extends IGene> void mutateInner(Individuum<T, U> i);

    public int getMutationCount() {
        return mutationCount;
    }

    public static MutationStrategy get(MutationType mutationType) {
        return switch (mutationType) {
            case CIM -> new CIM(Configuration.INSTANCE.randomGenerator);
            case SCM -> new SCM(Configuration.INSTANCE.randomGenerator);

            default -> throw new RuntimeException(mutationType + " is not implemented yet.");
        };
    }
}
