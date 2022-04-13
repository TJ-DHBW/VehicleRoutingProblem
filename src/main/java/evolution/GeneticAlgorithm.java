package evolution;

import evolution.crossover.CrossoverStrategy;
import evolution.mutation.MutationStrategy;
import evolution.selection.SelectionStrategy;

import java.util.Random;

public class GeneticAlgorithm {
    private final Random randomGenerator;
    private final CrossoverStrategy crossoverStrategy;
    private final MutationStrategy mutationStrategy;
    private final SelectionStrategy selectionStrategy;
    private  Population population;

    public GeneticAlgorithm(Random randomGenerator, CrossoverStrategy crossoverStrategy, MutationStrategy mutationStrategy, SelectionStrategy selectionStrategy) {
        this.randomGenerator = randomGenerator;
        this.crossoverStrategy = crossoverStrategy;
        this.mutationStrategy = mutationStrategy;
        this.selectionStrategy = selectionStrategy;
    }

    public void buildInitialPopulation(int populationSize){

    }

    public void evolvePopulation(){

    }
}
