package app;

import evolution.GeneticAlgorithm;
import evolution.crossover.CrossoverStrategy;
import evolution.mutation.MutationStrategy;
import evolution.selection.SelectionStrategy;

public class Application {
    public static void main(String[] args) {
        System.out.println("Henlo Wöld!");

        Configuration configInstance = Configuration.INSTANCE;

        CrossoverStrategy crossoverStrategy = CrossoverStrategy.get(configInstance.crossoverType);
        MutationStrategy mutationStrategy = MutationStrategy.get(configInstance.mutationType);
        SelectionStrategy selectionStrategy = SelectionStrategy.get(configInstance.selectionType);
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(configInstance.randomGenerator, crossoverStrategy, mutationStrategy, selectionStrategy);

        geneticAlgorithm.buildInitialPopulation(configInstance.populationSize);
        geneticAlgorithm.evolvePopulation();
    }
}
