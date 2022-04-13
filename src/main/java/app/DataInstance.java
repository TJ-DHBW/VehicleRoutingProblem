package app;

import vrp.Customer;
import vrp.Depot;

import java.util.ArrayList;

public record DataInstance(String name, int vehiclesNum, int vehiclesCapacity, ArrayList<Depot> depots, ArrayList<Customer> customers) {
}
