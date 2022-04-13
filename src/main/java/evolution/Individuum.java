package evolution;

public class Individuum {
    private double fitness = Double.MAX_VALUE;
    private IGenotype genotype;

    public double getFitness() {
        if (this.fitness == Double.MAX_VALUE){
            this.fitness = genotype.calculateFitness();
        }
        return fitness;
    }

    public IGenotype getGenotype() {
        return genotype;
    }
}
