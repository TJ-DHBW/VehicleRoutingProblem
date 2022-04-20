package vrp;

import app.Configuration;
import app.DataInstance;
import evolution.GeneticAlgorithm;
import evolution.crossover.CrossoverType;
import evolution.mutation.MutationType;
import evolution.selection.SelectionType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class OptimisationLogger {
    private DataInstance dataInstance = null;
    private GeneticAlgorithm<Route, Customer> geneticAlgorithm = null;
    private Date startTime = null;
    private Date endTime = null;

    public void logToFile(Path logFilePath) throws IOException {
        if (dataInstance == null || geneticAlgorithm == null || startTime == null || endTime == null) throw new IllegalStateException("Logging is only possible after setting all data");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath.toFile()))) {
            writeHeader(writer);
            writer.newLine();
            writeDemandManagement(writer);
            writer.newLine();
            writeGeneticAlgorithm(writer);
            writer.newLine();
            writeRouteManagement(writer);
            writer.newLine();
            writeDataAnalytics(writer);
            writer.flush();
        }
    }

    private void writeDataAnalytics(BufferedWriter writer) throws IOException {
        ArrayList<Customer> championGenes = this.geneticAlgorithm.getPopulation().getChampion().getGenotype().getGenes();
        ArrayList<ArrayList<Customer>> subRoutes = Route.splitToSubRoutes(championGenes, dataInstance.getVehiclesCapacity());
        if (subRoutes.size() <= 0) throw new IllegalStateException("The amount of sub routes should never be zero.");

        List<List<Integer>> routeTimings = subRoutes.stream().map(subRoute -> {
            int currentTime = 0;
            for (Customer customer : subRoute) {
                if (currentTime < customer.readyTime()) {
                    currentTime = customer.readyTime();
                }
                currentTime += customer.serviceTime();
            }
            return List.of(subRoute.get(0).readyTime(), currentTime);
        }).sorted(Comparator.comparingInt(timings -> timings.get(0))).toList();
        List<Integer> routeLoads = subRoutes.stream().map(subRoute -> subRoute.stream().map(Customer::demand).reduce(0, Integer::sum)).toList();
        List<Double> routeDistances = subRoutes.stream().map(subRoute -> {
            int depotId = dataInstance.getDepots().get(0).getId();
            int currentId = depotId;
            double totalDistance = 0.0;
            for (Customer customer : subRoute) {
                totalDistance += dataInstance.getDistanceMatrix()[currentId][customer.getId()];
                currentId = customer.getId();
            }
            totalDistance += dataInstance.getDistanceMatrix()[currentId][depotId];
            return totalDistance;
        }).toList();

        // TODO: check if this gives the correct result
        int totalCountVehicleUsed;
        if(Configuration.INSTANCE.vrpMode == VRPMode.CVRP) {
            totalCountVehicleUsed = 1;
        }else {
            totalCountVehicleUsed = 0;
            int[] returnTimes = new int[dataInstance.getVehiclesNum()];
            for (List<Integer> timings : routeTimings) {
                if (returnTimes[0] == 0) totalCountVehicleUsed++;
                returnTimes[0] = timings.get(1) + Math.max(0, returnTimes[0]-timings.get(0));
                Arrays.sort(returnTimes);
            }
        }
        double totalCountVehicleUsedPercent = ((double) totalCountVehicleUsed / dataInstance.getVehiclesNum()) * 100;
        double averageLoadVehicle = ((double) (routeLoads.stream().reduce(0, Integer::sum)) / subRoutes.size());
        double averageLoadVehiclePercent = (averageLoadVehicle / dataInstance.getVehiclesCapacity()) * 100;
        int minimumLoadVehicle = routeLoads.stream().min(Integer::compareTo).orElse(-1);
        double minimumLoadVehiclePercent = ((double) minimumLoadVehicle / dataInstance.getVehiclesCapacity()) * 100;
        double totalDistanceTravelled = routeDistances.stream().reduce(0.0, Double::sum);
        double shortestRouteDistance = routeDistances.stream().min(Double::compareTo).orElse(-1.0);
        int shortestRoute = routeDistances.indexOf(shortestRouteDistance);
        double longestRouteDistance = routeDistances.stream().max(Double::compareTo).orElse(-1.0);
        int longestRoute = routeDistances.indexOf(longestRouteDistance);

        writeLines(writer, new String[]{"Statistics | Data Analytics",
                "totalCountVehicleUsed = %d (%.2f%%)".formatted(totalCountVehicleUsed, totalCountVehicleUsedPercent),
                "averageLoadVehicle = %.2f (%.2f%%)".formatted(averageLoadVehicle, averageLoadVehiclePercent),
                "minimumLoadVehicle = %d (%.2f%%)".formatted(minimumLoadVehicle, minimumLoadVehiclePercent),
                "totalDistanceTravelled = %.0f".formatted(totalDistanceTravelled),
                "shortestRoute = #%d (%.0f)".formatted(shortestRoute, shortestRouteDistance),
                "longestRoute = #%d (%.0f)".formatted(longestRoute, longestRouteDistance)
        });
    }

    private void writeRouteManagement(BufferedWriter writer) throws IOException {
        ArrayList<Customer> championGenes = this.geneticAlgorithm.getPopulation().getChampion().getGenotype().getGenes();
        ArrayList<ArrayList<Customer>> subRoutes = Route.splitToSubRoutes(championGenes, dataInstance.getVehiclesCapacity());

        String[] lines = new String[subRoutes.size()+1];
        lines[0] = "[Route Management]";

        for (int i = 0; i < subRoutes.size(); i++) {
            ArrayList<Customer> currentSubRoute = subRoutes.get(i);
            StringBuilder routeLine = new StringBuilder();
            routeLine.append("Route #").append(i+1).append(" |");
            for (Customer customer : currentSubRoute) {
                routeLine.append(" ").append(customer.id());
            }
            lines[i+1] = routeLine.toString();
        }

        writeLines(writer, lines);
    }

    private void writeGeneticAlgorithm(BufferedWriter writer) throws IOException {
        int maximumNumberIterations = Configuration.INSTANCE.maxGenerationCount;
        int populationSize = Configuration.INSTANCE.initialPopulationSize;
        // TODO: getElitism
        boolean isElitismEnabled = false;
        SelectionType selectionType = Configuration.INSTANCE.selectionType;
        String selectionName = selectionType.getFullName();
        int k = Configuration.INSTANCE.matingSelectionSize;
        CrossoverType crossoverType = Configuration.INSTANCE.crossoverType;
        String crossoverName = crossoverType.getFullName();
        double crossoverRatio = Configuration.INSTANCE.crossoverRate;
        MutationType mutationType = Configuration.INSTANCE.mutationType;
        String mutationName = mutationType.getFullName();
        double mutationRatio = Configuration.INSTANCE.mutationRate;
        int countCrossoverOperations = this.geneticAlgorithm.getCrossoverStrategy().getCrossoverCount();
        int countMutationOperations = this.geneticAlgorithm.getMutationStrategy().getMutationCount();

        writeLines(writer, new String[]{"[Genetic Algorithm]",
                "maximumNumberIterations = "+maximumNumberIterations,
                "populationSize = "+populationSize,
                "isElitismEnabled = "+isElitismEnabled,
                selectionName+" ("+selectionType+") | k = "+k,
                crossoverName+" ("+crossoverType+") | crossoverRatio = "+crossoverRatio,
                mutationName+" ("+mutationType+") | mutationRatio = "+mutationRatio,
                "countCrossoverOperations = "+countCrossoverOperations,
                "countMutationOperations = "+countMutationOperations});
    }

    private void writeDemandManagement(BufferedWriter writer) throws IOException {
        int totalCountVehicle = this.dataInstance.getVehiclesNum();
        int maximumCapacityVehicle = this.dataInstance.getVehiclesCapacity();
        int totalCountCustomer = this.dataInstance.getCustomers().size();
        int totalSumDemand = this.dataInstance.getCustomers().stream().map(Customer::demand).reduce(0, Integer::sum);

        writeLines(writer, new String[]{"[Demand Management]",
                "totalCountVehicle = "+totalCountVehicle,
                "maximumCapacityVehicle = "+maximumCapacityVehicle,
                "totalCountCustomer = "+totalCountCustomer,
                "totalSumDemand = "+totalSumDemand});
    }

    private void writeHeader(BufferedWriter writer) throws IOException {
        String executionDate = new SimpleDateFormat("EEEE, dd.MM.yyyy").format(this.startTime);
        String startTime = new SimpleDateFormat("HH:mm:ss").format(this.startTime);
        String endTime = new SimpleDateFormat("HH:mm:ss").format(this.endTime);
        long runtime = TimeUnit.SECONDS.convert(this.endTime.getTime() - this.startTime.getTime(), TimeUnit.MILLISECONDS);

        writeLines(writer, new String[]{
                "Optimisation executed on "+executionDate,
                "Started: "+startTime+" | Finished: "+endTime+" | Runtime: "+runtime+" seconds"
        });
    }

    private void writeLines(BufferedWriter writer, String[] lines) throws IOException {
        for (String line : lines){
            writer.write(line);
            writer.newLine();
        }
    }

    public void setDataInstance(DataInstance dataInstance) {
        this.dataInstance = dataInstance;
    }

    public void setGeneticAlgorithm(GeneticAlgorithm<Route, Customer> geneticAlgorithm) {
        this.geneticAlgorithm = geneticAlgorithm;
    }

    public void setStartTime(long startTime) {
        this.startTime = new Date(startTime);
    }

    public void setEndTime(long endTime) {
        this.endTime = new Date(endTime);
    }
}
