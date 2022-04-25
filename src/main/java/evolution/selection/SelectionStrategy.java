package evolution.selection;

import app.Configuration;
import evolution.IGene;
import evolution.IGenotype;
import evolution.Individuum;

import java.util.ArrayList;

public abstract class SelectionStrategy {
    public abstract <T extends IGenotype<U>, U extends IGene> ArrayList<Individuum<T, U>> select(ArrayList<Individuum<T, U>> selectionPool, int selectionSize);


    public static SelectionStrategy get(SelectionType selectionType){
        return switch (selectionType){
            case RWS -> new RWS(Configuration.INSTANCE.randomGenerator);
            case TOS -> new TOS(Configuration.INSTANCE.randomGenerator);

            default -> throw new RuntimeException(selectionType+" is not implemented yet.");
        };
    }
}
