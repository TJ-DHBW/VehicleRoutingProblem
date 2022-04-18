package app;

import evolution.GeneticAlgorithm;
import evolution.crossover.CrossoverStrategy;
import evolution.mutation.MutationStrategy;
import evolution.selection.SelectionStrategy;
import vrp.Customer;
import vrp.FitnessRoute;
import vrp.OptimisationLogger;
import vrp.Route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

public class Application {
    // TODO: Do we need to implement Elitism?

    public static void main(String[] args) {
        DataInstance ukraineData = DataManagement.readData();

        Configuration configInstance = Configuration.INSTANCE;

        CrossoverStrategy crossoverStrategy = CrossoverStrategy.get(configInstance.crossoverType);
        MutationStrategy mutationStrategy = MutationStrategy.get(configInstance.mutationType);
        SelectionStrategy selectionStrategy = SelectionStrategy.get(configInstance.selectionType);
        GeneticAlgorithm<Route, Customer> geneticAlgorithm = new GeneticAlgorithm<>(configInstance.randomGenerator, crossoverStrategy, mutationStrategy, selectionStrategy);
        geneticAlgorithm.setSelectionSize(configInstance.matingSelectionSize);
        geneticAlgorithm.setCrossoverRate(configInstance.crossoverRate);
        geneticAlgorithm.setMutationRate(configInstance.mutationRate);

        FitnessRoute fitnessRoute = new FitnessRoute(ukraineData);
        fitnessRoute.setPenaltyPer1Lateness(configInstance.penaltyPer1Lateness);
        Function<ArrayList<Customer>, Double> fitnessFunction = switch (Configuration.INSTANCE.vrpMode) {
            case CVRP -> fitnessRoute::nonTimeWindow;
            case CVRPTW -> fitnessRoute::timeWindow;

            default -> throw new RuntimeException("VRPMode "+Configuration.INSTANCE.vrpMode+" is not yet implemented.");
        };
        geneticAlgorithm.initialize(configInstance.initialPopulationSize,
                ukraineData.customers(),
                Route::new,
                fitnessFunction);

        OptimisationLogger logger = new OptimisationLogger();
        logger.setDataInstance(ukraineData);
        logger.setGeneticAlgorithm(geneticAlgorithm);
        logger.setStartTime(System.currentTimeMillis());
        geneticAlgorithm.evolvePopulation(configInstance.maxGenerationCount);
        logger.setEndTime(System.currentTimeMillis());

        try {
            logger.logToFile(configInstance.logPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Was not able to write the logfile");
        }
    }
}
