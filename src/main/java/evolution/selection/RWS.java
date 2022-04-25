package evolution.selection;

import evolution.IGene;
import evolution.IGenotype;
import evolution.Individuum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class RWS extends SelectionStrategy {
    private final Random randomGenerator;

    public RWS(Random randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    //TODO untested



    private <T extends IGenotype<U>, U extends IGene> void addIndividuumByProbability(HashSet<Individuum<T,U>> matingPoolSet, ArrayList<Individuum<T,U>> matingPool, Double totalFitness) {
        Double randomSelectValue = randomGenerator.nextDouble()*totalFitness;
        double cumulativeFitness = 0.0;
        for(Individuum<T, U> individuum : matingPool){
            cumulativeFitness += individuum.getFitness();
            if(cumulativeFitness >= randomSelectValue){
                matingPoolSet.add(individuum);
                return;
            }
        }
    }

    @Override
    public <T extends IGenotype<U>, U extends IGene> ArrayList<Individuum<T, U>> select(ArrayList<Individuum<T, U>> selectionPool, int selectionSize) {

        if (selectionPool.size() < 1) return selectionPool;

        HashSet<Individuum<T, U>> matingPoolSet = new HashSet<>();
        Double totalFitness = 0.0;
        for(Individuum<T, U> individuum : selectionPool){
            totalFitness += individuum.getFitness();
        }

        while(matingPoolSet.size() <  selectionSize){
            addIndividuumByProbability(matingPoolSet, selectionPool, totalFitness);
        }
        return new ArrayList<>(matingPoolSet);
    }
}
