package evolution;

import evolution.crossover.CrossoverStrategy;
import evolution.mutation.MutationStrategy;
import evolution.selection.SelectionStrategy;

import java.util.*;
import java.util.function.Function;

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

    public <T extends IGenotype<U>, U extends IGene> void initialize(int populationSize, Collection<U> genePool, Function<ArrayList<U>, T> genotypeConstructor, Function<ArrayList<U>, Double> fitnessFunction){
        ArrayList<Individuum<T, U>> individuums = new ArrayList<>();
        ArrayList<U> availableGenes = new ArrayList<>(genePool);

        for (int i = 0; i < populationSize; i++) {
            Collections.shuffle(availableGenes);
            T genotype = genotypeConstructor.apply(availableGenes);
            individuums.add(new Individuum<>(genotype, fitnessFunction));
        }

        population = new Population(individuums);
    }

    public void evolvePopulation(int maxGenerationCount){
        int generationCount = 0;

        while (generationCount < maxGenerationCount) {
            generationCount++;

            // TODO: Configure selectionSize
            ArrayList<Individuum<?, ?>> matingPool = select(this.population.getIndividuums(), 1);

            ArrayList<Individuum<?, ?>> children = makeLoveNotWar(matingPool);
            mutate(children);

            int numToExterminate = (int) (this.randomGenerator.nextDouble() * this.population.getIndividuums().size() * 0.1);
            this.population.exterminateStragglers(numToExterminate);

            this.population.getIndividuums().addAll(children);
        }
    }

    private ArrayList<Individuum<?, ?>> select(ArrayList<Individuum<?, ?>> individuums, int selectionSize) {
        return this.selectionStrategy.select(individuums, selectionSize);
    }

    // TODO: Probably rename :(
    private ArrayList<Individuum<?, ?>> makeLoveNotWar(List<Individuum<?, ?>> matingPool){
        Collections.shuffle(matingPool);
        ArrayList<Individuum<?, ?>> children = new ArrayList<>();

        for (int i = 1; i < matingPool.size(); i += 2) {
            // TODO: Configure crossoverRate
            if (this.randomGenerator.nextDouble() < 0.5) continue;

            ArrayList<Individuum<?, ?>> newChildren = this.crossoverStrategy.execute(matingPool.get(i-1), matingPool.get(i));
            children.addAll(newChildren);
        }

        return children;
    }

    private void mutate(ArrayList<Individuum<?, ?>> individuums){
        individuums.forEach(individuum -> {
            // TODO: Configure mutationRate
            if (this.randomGenerator.nextDouble() < 0.5) return;
            this.mutationStrategy.mutate(individuum);
        });
    }
}
