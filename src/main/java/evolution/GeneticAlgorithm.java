package evolution;

import app.Configuration;
import evolution.crossover.CrossoverStrategy;
import evolution.mutation.MutationStrategy;
import evolution.selection.SelectionStrategy;

import java.util.*;
import java.util.function.Function;

public class GeneticAlgorithm<T extends IGenotype<U>, U extends IGene> {
    private final Random randomGenerator;
    private final CrossoverStrategy crossoverStrategy;
    private final MutationStrategy mutationStrategy;
    private final SelectionStrategy selectionStrategy;
    private Population<T, U> population;

    private int selectionSize = 100;
    private double crossoverRate = 0.7;
    private double mutationRate = 0.003;

    public GeneticAlgorithm(Random randomGenerator, CrossoverStrategy crossoverStrategy, MutationStrategy mutationStrategy, SelectionStrategy selectionStrategy) {
        this.randomGenerator = randomGenerator;
        this.crossoverStrategy = crossoverStrategy;
        this.mutationStrategy = mutationStrategy;
        this.selectionStrategy = selectionStrategy;
    }

    public void initialize(int populationSize, Collection<U> genePool, Function<ArrayList<U>, T> genotypeConstructor, Function<ArrayList<U>, Double> fitnessFunction) {
        ArrayList<Individuum<T, U>> individuums = new ArrayList<>();
        ArrayList<U> availableGenes = new ArrayList<>(genePool);

        for (int i = 0; i < populationSize; i++) {
            Collections.shuffle(availableGenes);
            T genotype = genotypeConstructor.apply(availableGenes);
            individuums.add(new Individuum<>(genotype, fitnessFunction));
        }

        population = new Population<>(individuums);
    }

    public void evolvePopulation(int maxGenerationCount) {
        int generationCount = 0;

        while (generationCount < maxGenerationCount) {
            generationCount++;

            if (Configuration.INSTANCE.continuousFitnessValues) {
                String logString = String.format("Generation: %4d | Best fitness: %5.5g", generationCount, this.population.getChampion().getFitness());
                System.out.println(logString);
            }

            ArrayList<Individuum<T, U>> matingPool = select(this.population.getIndividuums(), this.selectionSize);

            ArrayList<Individuum<T, U>> children = makeLoveNotWar(matingPool);
            mutate(children);

            int numToExterminate = (int) (this.randomGenerator.nextDouble() * this.population.getIndividuums().size() * 0.1);
            this.population.exterminateStragglers(numToExterminate, Configuration.INSTANCE.useElitism);

            this.population.getIndividuums().addAll(children);
        }
    }

    private ArrayList<Individuum<T, U>> select(ArrayList<Individuum<T, U>> individuums, int selectionSize) {
        return this.selectionStrategy.select(individuums, selectionSize);
    }

    private ArrayList<Individuum<T, U>> makeLoveNotWar(List<Individuum<T, U>> matingPool) {
        Collections.shuffle(matingPool);
        ArrayList<Individuum<T, U>> children = new ArrayList<>();

        for (int i = 1; i < matingPool.size(); i += 2) {
            if (this.randomGenerator.nextDouble() < this.crossoverRate) continue;

            ArrayList<Individuum<T, U>> newChildren = this.crossoverStrategy.execute(matingPool.get(i - 1), matingPool.get(i));
            children.addAll(newChildren);
        }

        return children;
    }

    private void mutate(ArrayList<Individuum<T, U>> individuums) {
        individuums.forEach(individuum -> {
            if (this.randomGenerator.nextDouble() < this.mutationRate) return;
            this.mutationStrategy.mutate(individuum);
        });
    }

    public Population<T, U> getPopulation() {
        return population;
    }

    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }

    public MutationStrategy getMutationStrategy() {
        return mutationStrategy;
    }

    public void setSelectionSize(int selectionSize) {
        this.selectionSize = selectionSize;
    }

    public void setCrossoverRate(double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }
}
