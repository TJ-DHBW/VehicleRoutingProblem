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

    public Individuum<T, U> createCopy(){
        @SuppressWarnings("unchecked")
        T genotypeCopy = (T) this.genotype.createCopy();
        return new Individuum<>(genotypeCopy, this.fitnessFunction);
    }

    public T getGenotype() {
        return genotype;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Individuum<?, ?> otherIndividuum)) return false;

        return this.genotype.equals(otherIndividuum.genotype) && this.fitnessFunction.equals(otherIndividuum.fitnessFunction);
    }
}
