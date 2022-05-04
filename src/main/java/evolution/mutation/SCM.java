package evolution.mutation;

import evolution.IGene;
import evolution.IGenotype;
import evolution.Individuum;

import java.util.*;

public class SCM extends MutationStrategy{
    private final Random randomGenerator;

    public SCM(Random randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    @Override
    protected <T extends IGenotype<U>, U extends IGene> void mutateInner(Individuum<T, U> i) {
        int sizeOfScramble = Math.max(i.getGenotype().getGenes().size()/2,2);
        int placeOfScramble = randomGenerator.nextInt(i.getGenotype().getGenes().size()-sizeOfScramble-1);

        ArrayList<U> genes = i.getGenotype().getGenes();
        ArrayList<U> subGenes = new ArrayList<>();
        for(int j = placeOfScramble; j < placeOfScramble+sizeOfScramble; j++){
            subGenes.add(i.getGenotype().getGenes().get(j));
        }
        List<U> geneList = new ArrayList<>(subGenes);
        while (geneList.equals(subGenes)){
            Collections.shuffle(subGenes);
        }
        for(int j = placeOfScramble; j < placeOfScramble+sizeOfScramble; j++) {
            genes.set(j, subGenes.get(j - placeOfScramble));
        }
    }
}
