package vrp;

import app.DataInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FitnessRoute {
    // FIXME: Adjust this value.
    private final int penaltyPer1Lateness = 10;
    private final DataInstance dataInstance;

    public FitnessRoute(DataInstance dataInstance){
        this.dataInstance = dataInstance;
    }

    // TODO: Test fitness function for VRP
    public double nonTimeWindow(ArrayList<Customer> route){
        if (dataInstance.depots().size() < 1) throw new IllegalStateException("Can not calculate fitness for a route without a depot");

        double totalDistance = 0;

        int vehicleCapacity = dataInstance.vehiclesCapacity();
        int depotX = dataInstance.depots().get(0).getX();
        int depotY = dataInstance.depots().get(0).getY();

        int vehicleX = depotX;
        int vehicleY = depotY;
        int vehicleSupplies = vehicleCapacity;

        for (Customer nextCustomer : route){
            if (nextCustomer.demand() > vehicleSupplies) {
                totalDistance += distanceFunction(vehicleX, vehicleY, depotX, depotY);
                vehicleX = depotX;
                vehicleY = depotY;
                vehicleSupplies = vehicleCapacity;
            }

            totalDistance += distanceFunction(vehicleX, vehicleY, nextCustomer.getX(), nextCustomer.getY());
            vehicleX = nextCustomer.getX();
            vehicleY = nextCustomer.getY();
            vehicleSupplies -= nextCustomer.demand();
        }

        totalDistance += distanceFunction(vehicleX, vehicleY, depotX, depotY);

        return totalDistance;
    }

    // TODO: Implement fitness function for VRPTW
    // TODO: This might be possible faster and better (especially the timing stuff)
    // TODO: Test this
    public double timeWindow(ArrayList<Customer> route){
        if (dataInstance.depots().size() < 1) throw new IllegalStateException("Can not calculate fitness for a route without a depot");

        double totalFitness = 0;

        int depotX = dataInstance.depots().get(0).getX();
        int depotY = dataInstance.depots().get(0).getY();

        ArrayList<ArrayList<Customer>> routes = splitToSubRoutes(route);
        routes.sort(Comparator.comparingInt(subRoute -> subRoute.get(0).readyTime()));

        int[] vehicleReadyTimes = new int[dataInstance.vehiclesCapacity()];
        for (ArrayList<Customer> subRoute : routes) {
            int currentTime = vehicleReadyTimes[0];
            int vehicleX = depotX;
            int vehicleY = depotY;

            for (Customer customer : subRoute) {
                if (currentTime > customer.dueDate()) {
                    totalFitness += penaltyPer1Lateness * (currentTime-customer.dueDate());
                }else if (currentTime < customer.readyTime()) {
                    currentTime = customer.readyTime();
                }
                currentTime += customer.serviceTime();
                totalFitness += distanceFunction(vehicleX, vehicleY, customer.getX(), customer.getY());
                vehicleX = customer.getX();
                vehicleY = customer.getY();
            }

            totalFitness += distanceFunction(vehicleX, vehicleY, depotX, depotY);
            vehicleReadyTimes[0] = currentTime;
            Arrays.sort(vehicleReadyTimes);
        }

        return totalFitness;
    }

    private ArrayList<ArrayList<Customer>> splitToSubRoutes(ArrayList<Customer> completeRoute) {
        ArrayList<ArrayList<Customer>> routes = new ArrayList<>();
        int customersServed;
        for (int i = 0; i < completeRoute.size(); i += customersServed) {
            ArrayList<Customer> subRoute = new ArrayList<>();
            customersServed = 0;
            int supplyAccumulator = 0;
            int indexOffset = 0;
            while (i+indexOffset < completeRoute.size()){
                Customer nextCustomer = completeRoute.get(i+indexOffset);
                if (supplyAccumulator+ nextCustomer.demand() > dataInstance.vehiclesCapacity()) break;
                supplyAccumulator += nextCustomer.demand();
                customersServed++;
                subRoute.add(nextCustomer);
                indexOffset++;
            }
            routes.add(subRoute);
        }
        return routes;
    }

    // FIXME: Maybe we should use precalculated distances here, for the fastness.
    private static double distanceFunction(int x1, int y1, int x2, int y2) {
        int dx = x1-x2;
        int dy = y1-y2;

        return Math.sqrt(dx*dx + dy*dy);
    }
}
