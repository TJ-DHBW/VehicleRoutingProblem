package evolution;

import java.util.ArrayList;

public class Population {
    // TODO: Maybe this will be final? Depends on how it is used by the genetic algorithm. Could be nice because you can set an enforced size.
    private ArrayList<Individuum<?, ?>> individuums;

    public ArrayList<Individuum<?, ?>> getIndividuums() {
        return individuums;
    }
}
