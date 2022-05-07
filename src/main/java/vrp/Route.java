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

    public static ArrayList<ArrayList<Customer>> splitToSubRoutes(ArrayList<Customer> completeRoute, int vehicleCapacity) {
        ArrayList<ArrayList<Customer>> routes = new ArrayList<>();
        int customersServed;
        for (int i = 0; i < completeRoute.size(); i += customersServed) {
            ArrayList<Customer> subRoute = new ArrayList<>();
            customersServed = 0;
            int supplyAccumulator = 0;
            int indexOffset = 0;
            while (i + indexOffset < completeRoute.size()) {
                Customer nextCustomer = completeRoute.get(i + indexOffset);
                if (supplyAccumulator + nextCustomer.demand() > vehicleCapacity) break;
                supplyAccumulator += nextCustomer.demand();
                customersServed++;
                subRoute.add(nextCustomer);
                indexOffset++;
            }
            routes.add(subRoute);
        }
        return routes;
    }
}
