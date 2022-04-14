package vrp;

import evolution.IGenotype;

import java.util.ArrayList;

public class Route implements IGenotype<Customer> {
    private final ArrayList<Customer> route;

    public Route(ArrayList<Customer> route) {
        this.route = route;
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
