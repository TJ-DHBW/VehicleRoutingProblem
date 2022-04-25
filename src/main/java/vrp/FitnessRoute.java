package vrp;

import app.DataInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FitnessRoute {
    private final DataInstance dataInstance;
    private int penaltyPer1Lateness = 10;

    public FitnessRoute(DataInstance dataInstance){
        this.dataInstance = dataInstance;
    }

    public double nonTimeWindow(ArrayList<Customer> route){
        if (dataInstance.getDepots().size() < 1) throw new IllegalStateException("Can not calculate fitness for a route without a depot");

        double totalDistance = 0;

        int vehicleCapacity = dataInstance.getVehiclesCapacity();
        int depotId = dataInstance.getDepots().get(0).getId();

        int currentId = depotId;
        int vehicleSupplies = vehicleCapacity;

        for (Customer nextCustomer : route){
            if (nextCustomer.demand() > vehicleSupplies) {
                totalDistance += dataInstance.getDistanceMatrix()[currentId][depotId];
                currentId = depotId;
                vehicleSupplies = vehicleCapacity;
            }

            totalDistance += dataInstance.getDistanceMatrix()[currentId][nextCustomer.getId()];
            currentId = nextCustomer.getId();
            vehicleSupplies -= nextCustomer.demand();
        }

        totalDistance += dataInstance.getDistanceMatrix()[currentId][depotId];

        return totalDistance;
    }

    // TODO: This might be possible faster and better (especially the timing stuff)
    public double timeWindow(ArrayList<Customer> route){
        if (dataInstance.getDepots().size() < 1) throw new IllegalStateException("Can not calculate fitness for a route without a depot");

        double totalFitness = 0;

        int depotId = dataInstance.getDepots().get(0).getId();

        ArrayList<ArrayList<Customer>> routes = Route.splitToSubRoutes(route, dataInstance.getVehiclesCapacity());
        routes.sort(Comparator.comparingInt(subRoute -> subRoute.get(0).readyTime()));

        int[] vehicleReadyTimes = new int[dataInstance.getVehiclesNum()];
        for (ArrayList<Customer> subRoute : routes) {
            int currentTime = vehicleReadyTimes[0];
            int currentId = depotId;

            for (Customer nextCustomer : subRoute) {
                if (currentTime > nextCustomer.dueDate()-nextCustomer.serviceTime()) {
                    totalFitness += penaltyPer1Lateness * (currentTime - (nextCustomer.dueDate()-nextCustomer.serviceTime()));
                }else if (currentTime < nextCustomer.readyTime()) {
                    currentTime = nextCustomer.readyTime();
                }
                currentTime += nextCustomer.serviceTime();
                totalFitness += dataInstance.getDistanceMatrix()[currentId][nextCustomer.getId()];
                currentId = nextCustomer.getId();
            }

            totalFitness += dataInstance.getDistanceMatrix()[currentId][depotId];
            vehicleReadyTimes[0] = currentTime;
            Arrays.sort(vehicleReadyTimes);
        }

        return totalFitness;
    }

    public void setPenaltyPer1Lateness(int penaltyPer1Lateness) {
        this.penaltyPer1Lateness = penaltyPer1Lateness;
    }
}
