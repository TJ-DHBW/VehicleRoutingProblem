package vrp;

import java.util.ArrayList;

public class FitnessRoute {
    private FitnessRoute(){}

    // TODO: Implement fitness function for VRP
    public static double nonTimeWindow(ArrayList<Customer> route){
        return 1.0;
    }

    // TODO: Implement fitness function for VRPTW
    public static double timeWindow(ArrayList<Customer> route){
        return 1.0;
    }
}
