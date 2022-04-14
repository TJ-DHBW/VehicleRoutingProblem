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

public class OptimisationLogger {
    private DataInstance dataInstance = null;
    private GeneticAlgorithm geneticAlgorithm = null;

    // TODO: Execution start time
    // TODO: Execution finish time

    public void logToFile(Path logFilePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath.toFile()))) {
            writeHeader(writer);
            writeDemandManagement(writer);
            writeGeneticAlgorithm(writer);
            writeRouteManagement(writer);
            writeDataAnalytics(writer);
            writer.flush();
        }
    }

    private void writeDataAnalytics(BufferedWriter writer) {
        // TODO: Logging
    }

    private void writeRouteManagement(BufferedWriter writer) {
        // TODO: Logging
    }

    private void writeGeneticAlgorithm(BufferedWriter writer) throws IOException {
        int maximumNumberIterations = Configuration.INSTANCE.maxGenerationCount;
        int populationSize = Configuration.INSTANCE.initialPopulationSize;
        // TODO: getElitism
        boolean isElitismEnabled = false;
        SelectionType selectionType = Configuration.INSTANCE.selectionType;
        // TODO: getSelectionName
        String selectionName = "";
        int k = Configuration.INSTANCE.matingSelectionSize;
        CrossoverType crossoverType = Configuration.INSTANCE.crossoverType;
        // TODO: getCrossoverName
        String crossoverName = "";
        double crossoverRatio = Configuration.INSTANCE.crossoverRate;
        MutationType mutationType = Configuration.INSTANCE.mutationType;
        // TODO: getMutationName
        String mutationName = "";
        double mutationRatio = Configuration.INSTANCE.mutationRate;
        // TODO: count them
        int countCrossoverOperations = 0;
        // TODO: count them
        int countMutationOperations = 0;

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
        int totalCountVehicle = this.dataInstance.vehiclesNum();
        int maximumCapacityVehicle = this.dataInstance.vehiclesCapacity();
        int totalCountCustomer = this.dataInstance.customers().size();
        int totalSumDemand = this.dataInstance.customers().stream().map(Customer::demand).reduce(0, Integer::sum);

        writeLines(writer, new String[]{"[Demand Management]",
                "totalCountVehicle = "+totalCountVehicle,
                "maximumCapacityVehicle = "+maximumCapacityVehicle,
                "totalCountCustomer = "+totalCountCustomer,
                "totalSumDemand = "+totalSumDemand});
    }

    private void writeHeader(BufferedWriter writer) {
        // TODO: Logging
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

    public void setGeneticAlgorithm(GeneticAlgorithm geneticAlgorithm) {
        this.geneticAlgorithm = geneticAlgorithm;
    }
}
