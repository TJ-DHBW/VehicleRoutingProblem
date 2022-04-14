package evolution.crossover;

import evolution.IGene;
import evolution.Individuum;

import java.security.DrbgParameters;
import java.util.*;

public class HRX extends CrossoverStrategy{
    private final Random randomGenerator;

    public HRX(Random randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    @Override
    ArrayList<Individuum<?, ?>> execute(Individuum<?, ?> parent1, Individuum<?, ?> parent2) {
        ArrayList<Individuum<?, ?>> children = new ArrayList<>();

        children.add(getChildWithCossover(parent1, parent2));

        return null;
    }

    private Individuum<?,?> getChildWithCossover(Individuum<?,?> parent1, Individuum<?,?> parent2) {
        //TODO muss noch raus
        Individuum<?, ?> returnIndividuum = null;
        //TODO funktion in anderem commit erstellt
        // Individuum<?, ?> returnIndividuum = parent1.createCopy;
        ArrayList<IGene> remainingGenes = new ArrayList<>(parent1.getGenotype().getGenes());
        Collections.shuffle(remainingGenes);

        Set<IGene> geneSet = new HashSet<>();
        ArrayList<IGene> crossedOverGenes = new ArrayList<>();
        IGene currentGene = null;
        while(geneSet.size() != parent1.getGenotype().getGenes().size()){
            if(currentGene == null){
                if(remainingGenes.size() == 0){
                    return mapCrossoverGenesToIndividuum(returnIndividuum, crossedOverGenes);
                }
                currentGene = getRandomGeneNotUsed(geneSet, remainingGenes);
                crossedOverGenes.add(currentGene);
            }
        }


    }

    private Individuum<?,?> mapCrossoverGenesToIndividuum(Individuum<?,?> returnIndividuum, ArrayList<? extends IGene> crossedOverGenes) {
        for(int i = 0; i < crossedOverGenes.size(); i++){
            returnIndividuum.getGenotype().getGenes().set(i, crossedOverGenes.get(i));
        }
        return  returnIndividuum;
    }

}
