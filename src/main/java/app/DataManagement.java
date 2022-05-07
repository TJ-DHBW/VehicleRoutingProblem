package app;

import vrp.Customer;
import vrp.Depot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DataManagement {
    private DataManagement() {
    }

    public static DataInstance readData() {
        // See: https://stackoverflow.com/questions/3861989/preferred-way-of-loading-resources-in-java
        URL dataURL = Thread.currentThread().getContextClassLoader().getResource(Configuration.INSTANCE.dataName);
        if (dataURL == null) throw new RuntimeException("No data file found.");

        try (BufferedReader reader = new BufferedReader(new FileReader(dataURL.getPath(), StandardCharsets.UTF_8))) {
            return DataManagement.parseFile(reader);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while reading file: " + dataURL.getPath());
        }
    }

    private static DataInstance parseFile(BufferedReader reader) throws IOException {
        String name = "";
        int vehiclesNum = 0;
        int vehiclesCapacity = 0;
        ArrayList<Depot> depots = new ArrayList<>();
        ArrayList<Customer> customers = new ArrayList<>();

        String line;
        int lineNumber = 1;
        while ((line = reader.readLine()) != null) {
            if (lineNumber < 10) {
                if (lineNumber == 1) {
                    name = line;
                } else if (lineNumber == 5) {
                    String[] vehicleData = line.split(" ");
                    if (vehicleData.length < 2)
                        throw new RuntimeException("To few numbers in vehicle line " + lineNumber + ".");

                    try {
                        vehiclesNum = Integer.parseInt(vehicleData[0]);
                        vehiclesCapacity = Integer.parseInt(vehicleData[1]);
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Line #" + lineNumber + " contains something that is not an integer.");
                    }
                }
            } else {
                String[] customerData = line.split(" ");
                if (customerData.length < 7)
                    throw new RuntimeException("To few numbers in customer line " + lineNumber + ".");

                try {
                    int id = Integer.parseInt(customerData[0]);
                    int xCoordinate = Integer.parseInt(customerData[1]);
                    int yCoordinate = Integer.parseInt(customerData[2]);
                    int demand = Integer.parseInt(customerData[3]);
                    int readyTime = Integer.parseInt(customerData[4]);
                    int dueDate = Integer.parseInt(customerData[5]);
                    int serviceTime = Integer.parseInt(customerData[6]);

                    if (demand != 0) {
                        customers.add(new Customer(id, xCoordinate, yCoordinate, demand, readyTime, dueDate, serviceTime));
                    } else {
                        depots.add(new Depot(id, xCoordinate, yCoordinate));
                    }
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Line #" + lineNumber + " contains something that is not an integer.");
                }
            }

            lineNumber++;
        }

        return new DataInstance(name, vehiclesNum, vehiclesCapacity, depots, customers);
    }
}
