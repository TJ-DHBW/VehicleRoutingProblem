package vrp;

import app.Configuration;
import app.DataInstance;
import evolution.GeneticAlgorithm;
import evolution.Individuum;
import evolution.crossover.CrossoverType;
import evolution.mutation.MutationType;
import evolution.selection.SelectionType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class OptimisationLogger {
    private DataInstance dataInstance = null;
    private GeneticAlgorithm geneticAlgorithm = null;
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

    private void writeDataAnalytics(BufferedWriter writer) {
        // TODO: Logging
    }

    private void writeRouteManagement(BufferedWriter writer) throws IOException {
        ArrayList<Individuum<?, ?>> individuums = geneticAlgorithm.getPopulation().getIndividuums();
        String[] lines = new String[individuums.size()+1];
        lines[0] = "[Route Management]";

        for (int i = 0; i < individuums.size(); i++) {
            Route currentRoute = (Route) individuums.get(i).getGenotype();
            StringBuilder routeLine = new StringBuilder();
            routeLine.append("Route #").append(i).append(" |");
            for (Customer customer : currentRoute.getGenes()){
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

    public void setGeneticAlgorithm(GeneticAlgorithm geneticAlgorithm) {
        this.geneticAlgorithm = geneticAlgorithm;
    }

    public void setStartTime(long startTime) {
        this.startTime = new Date(startTime);
    }

    public void setEndTime(long endTime) {
        this.endTime = new Date(endTime);
    }
}
