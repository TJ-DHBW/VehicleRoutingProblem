package app;

import vrp.Customer;
import vrp.Depot;
import vrp.ILocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataInstance {
    private final String name;
    private final int vehiclesNum;
    private final int vehiclesCapacity;
    private final ArrayList<Depot> depots;
    private final ArrayList<Customer> customers;
    private final double[][] distanceMatrix;

    public DataInstance(String name, int vehiclesNum, int vehiclesCapacity, ArrayList<Depot> depots, ArrayList<Customer> customers) {
        this.name = name;
        this.vehiclesNum = vehiclesNum;
        this.vehiclesCapacity = vehiclesCapacity;
        this.depots = depots;
        this.customers = customers;
        this.distanceMatrix = this.calculateDistanceMatrix();
    }

    private double[][] calculateDistanceMatrix() {
        if (!this.hasContinuousIdsStartingFromZero()) throw new IllegalStateException("Distance matrix can only be calculated for continuous ids starting from 0.");
        int amountLocations = this.depots.size() + this.customers.size();
        double[][] ret = new double[amountLocations][amountLocations];

        HashMap<Integer, ILocation> locations = new HashMap<>();
        this.depots.forEach(depot -> locations.put(depot.getId(), depot));
        this.customers.forEach(customer -> locations.put(customer.getId(), customer));

        for (int i = 0; i < amountLocations; i++) {
            ILocation location1 = locations.get(i);
            for (int j = i; j < amountLocations; j++) {
                ILocation location2 = locations.get(j);
                if (i == j) {
                    ret[i][j] = 0.0;
                }else {
                    ret[i][j] = Math.sqrt(Math.pow((location1.getX()-location2.getX()), 2) + Math.pow((location1.getY()-location2.getY()), 2));
                    ret[j][i] = ret[i][j];
                }
            }
        }

        return ret;
    }

    private boolean hasContinuousIdsStartingFromZero() {
        List<Integer> depotIds = this.depots.stream().map(Depot::getId).toList();
        List<Integer> customerIds = this.customers.stream().map(Customer::getId).toList();

        int amountLocations = this.depots.size() + this.customers.size();

        for (int i = 0; i < amountLocations; i++) {
            if (!(depotIds.contains(i) || customerIds.contains(i))) {
                return false;
            }
        }

        return true;
    }

    public int getVehiclesNum() {
        return vehiclesNum;
    }

    public int getVehiclesCapacity() {
        return vehiclesCapacity;
    }

    public ArrayList<Depot> getDepots() {
        return depots;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public double[][] getDistanceMatrix() {
        return distanceMatrix;
    }
}
