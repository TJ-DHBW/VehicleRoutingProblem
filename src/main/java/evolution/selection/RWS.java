package evolution.selection;

import evolution.Individuum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class RWS extends SelectionStrategy {
    private final Random randomGenerator;

    public RWS(Random randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    //TODO untested

    @Override
    ArrayList<Individuum<?, ?>> select(ArrayList<Individuum<?, ?>> selectionPool, int selectionSize) {
        ArrayList<Individuum<?, ?>> matingPool = new ArrayList<>();

        if (selectionPool.size() < 1) return matingPool;

        HashSet<Individuum<?, ?>> matingPoolSet = new HashSet<>();
        Double totalFitness = 0.0;
        for(Individuum<?, ?> individuum : matingPool){
            totalFitness += individuum.getFitness();
        }

        while(matingPoolSet.size() <  selectionSize){
            addIndividuumByProbability(matingPoolSet, matingPool, totalFitness);
        }
        return matingPool;
    }

    private void addIndividuumByProbability(HashSet<Individuum<?,?>> matingPoolSet, ArrayList<Individuum<?,?>> matingPool, Double totalFitness) {
        Double randomSelectValue = randomGenerator.nextDouble()*totalFitness;
        double cumulativeFitness = 0.0;
        for(Individuum<?, ?> individuum : matingPool){
            cumulativeFitness += individuum.getFitness();
            if(cumulativeFitness >= randomSelectValue){
                matingPoolSet.add(individuum);
                return;
            }
        }
    }
}
