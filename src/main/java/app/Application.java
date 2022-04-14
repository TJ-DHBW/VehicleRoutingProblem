package app;

import evolution.GeneticAlgorithm;
import evolution.crossover.CrossoverStrategy;
import evolution.mutation.MutationStrategy;
import evolution.selection.SelectionStrategy;
import vrp.Customer;
import vrp.FitnessRoute;
import vrp.Route;

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
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(configInstance.randomGenerator, crossoverStrategy, mutationStrategy, selectionStrategy);

        Function<ArrayList<Customer>, Double> fitnessFunction = getFitnessFunction();
        geneticAlgorithm.initialize(configInstance.initialPopulationSize,
                ukraineData.customers(),
                Route::new,
                fitnessFunction);
        geneticAlgorithm.evolvePopulation(configInstance.maxGenerationCount);
    }

    private static Function<ArrayList<Customer>, Double> getFitnessFunction(){
        return switch (Configuration.INSTANCE.vrpMode) {
            case CVRP -> FitnessRoute::nonTimeWindow;
            case CVRPTW -> FitnessRoute::timeWindow;

            default -> throw new RuntimeException("VRPMode "+Configuration.INSTANCE.vrpMode+" is not yet implemented.");
        };
    }
}
