package evolution;

import java.util.ArrayList;
import java.util.function.Function;

public class Individuum<T extends IGenotype<U>, U extends IGene> {
    private double fitness = Double.MAX_VALUE;
    private final T genotype;
    private final Function<ArrayList<U>, Double> fitnessFunction;

    public Individuum(T genotype, Function<ArrayList<U>, Double> fitnessFunction){
        this.genotype = genotype;
        this.fitnessFunction = fitnessFunction;
    }

    public double getFitness() {
        if (this.fitness == Double.MAX_VALUE){
            this.fitness = this.fitnessFunction.apply(this.genotype.getGenes());
        }
        return fitness;
    }

    public T getGenotype() {
        return genotype;
    }
}
