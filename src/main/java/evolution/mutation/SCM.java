package evolution.mutation;

import evolution.Individuum;

import java.util.*;

public class SCM extends MutationStrategy{
    private final Random randomGenerator;

    public SCM(Random randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    @Override
    void mutate(Individuum<?, ?> individuum) {
        mutateHelper(individuum);
    }

    // TODO Test this implementation
    private <T> void mutateHelper(Individuum<?, ?> individuum){
        int sizeOfScramble = individuum.getGenotype().getGenes().size()/5;
        int placeOfScramble = randomGenerator.nextInt(individuum.getGenotype().getGenes().size()-sizeOfScramble-1);

        @SuppressWarnings("unchecked")
        ArrayList<T> genes = (ArrayList<T>) individuum.getGenotype().getGenes();
        ArrayList<T> subGenes = new ArrayList<>();
        for(int i = placeOfScramble; i < placeOfScramble+sizeOfScramble; i++){
            subGenes.add((T) individuum.getGenotype().getGenes().get(i));
        }
        List<T> geneList = new ArrayList<>(subGenes);
        while (geneList.equals(subGenes)){
            Collections.shuffle(subGenes);
        }
        for(int i = placeOfScramble; i < placeOfScramble+sizeOfScramble; i++) {
            genes.set(i, subGenes.get(i - placeOfScramble));
        }
    }
}
