package app;

import evolution.GeneticAlgorithm;
import evolution.Individuum;
import evolution.crossover.CrossoverStrategy;
import evolution.mutation.MutationStrategy;
import evolution.selection.SelectionStrategy;

import java.util.ArrayList;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        DataInstance ukraineData = DataManagement.readData();

        Configuration configInstance = Configuration.INSTANCE;

        CrossoverStrategy crossoverStrategy = CrossoverStrategy.get(configInstance.crossoverType);
        MutationStrategy mutationStrategy = MutationStrategy.get(configInstance.mutationType);
        SelectionStrategy selectionStrategy = SelectionStrategy.get(configInstance.selectionType);
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(configInstance.randomGenerator, crossoverStrategy, mutationStrategy, selectionStrategy);

        geneticAlgorithm.buildInitialPopulation(ukraineData.customers(), configInstance.populationSize);
        geneticAlgorithm.evolvePopulation();
    }
}
