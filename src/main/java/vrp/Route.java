package vrp;

import evolution.IGenotype;

import java.util.ArrayList;
import java.util.Collection;

public class Route implements IGenotype<Customer> {
    private final ArrayList<Customer> route;

    public Route(Collection<Customer> route) {
        this.route = new ArrayList<>(route);
    }

    @Override
    public ArrayList<Customer> getGenes() {
        return route;
    }

    @Override
    public IGenotype<Customer> createCopy() {
        return new Route(new ArrayList<>(this.route));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IGenotype<?> otherRoute)) return false;

        return route.equals(otherRoute.getGenes());
    }
}
