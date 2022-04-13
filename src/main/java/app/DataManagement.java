package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DataManagement {
    private DataManagement(){
    }

    // TODO: Call this at an appropriate time
    public static void readData() {
        // See: https://stackoverflow.com/questions/3861989/preferred-way-of-loading-resources-in-java
        URL dataURL = Thread.currentThread().getContextClassLoader().getResource("data.txt");
        if (dataURL == null) throw new RuntimeException("No data file found.");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataURL.getPath(), StandardCharsets.UTF_8));

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                DataManagement.handleLine(line, lineNumber);
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: Actually do something with the data
    private static void handleLine(String line, int lineNumber){
        if (lineNumber < 10){
            if (lineNumber == 1) {
                System.out.println("Instance: "+line);
            }else if (lineNumber == 5) {
                String[] vehicleData = line.split(" ");
                if (vehicleData.length < 2) throw new RuntimeException("To few numbers in vehicle line "+lineNumber+".");

                try {
                    int vehiclesNum = Integer.parseInt(vehicleData[0]);
                    int vehiclesCapacity = Integer.parseInt(vehicleData[1]);
                    System.out.println("Num vehicles: "+vehiclesNum+", vehicle capacity: "+vehiclesCapacity);
                }catch (NumberFormatException e) {
                    throw new RuntimeException("Line #"+lineNumber+" contains something that is not an integer.");
                }
            }
        }else{
            String[] customerData = line.split(" ");
            if (customerData.length < 7) throw new RuntimeException("To few numbers in customer line "+lineNumber+".");

            try {
                int id = Integer.parseInt(customerData[0]);
                int xCoordinate = Integer.parseInt(customerData[1]);
                int yCoordinate = Integer.parseInt(customerData[2]);
                int demand = Integer.parseInt(customerData[3]);
                int readyTime = Integer.parseInt(customerData[4]);
                int dueDate = Integer.parseInt(customerData[5]);
                int serviceTime = Integer.parseInt(customerData[6]);
                System.out.println(id+" | "+xCoordinate+" | "+yCoordinate+" | "+demand+" | "+readyTime+" | "+dueDate+" | "+serviceTime);
            }catch (NumberFormatException e) {
                throw new RuntimeException("Line #"+lineNumber+" contains something that is not an integer.");
            }
        }
    }
}
