package evolution.selection;

import app.Configuration;
import evolution.Individuum;

import java.util.ArrayList;

public abstract class SelectionStrategy {
    abstract ArrayList<Individuum> select(ArrayList<Individuum> selectionPool, int selectionSize);


    // TODO: register implementations
    public static SelectionStrategy get(SelectionType selectionType){
        return switch (selectionType){
            case RWS -> throw new RuntimeException("RWS is not implemented yet.");
            case TOS -> new TOS(Configuration.INSTANCE.randomGenerator);

            default -> throw new RuntimeException(selectionType+" is not implemented yet.");
        };
    }
}
