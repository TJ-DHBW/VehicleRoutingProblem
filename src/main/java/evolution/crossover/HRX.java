package evolution.crossover;

import evolution.IGene;
import evolution.IGenotype;
import evolution.Individuum;

import java.util.*;

public class HRX extends CrossoverStrategy {
    private final Random randomGenerator;

    public HRX(Random randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    private <T extends IGenotype<U>, U extends IGene> Individuum<T, U> getChildWithCossover(Individuum<T, U> parent1, Individuum<T, U> parent2) {
        Individuum<T, U> returnIndividuum = parent1.createCopy();

        ArrayList<U> remainingGenes = new ArrayList<>(parent1.getGenotype().getGenes());
        Collections.shuffle(remainingGenes);

        Set<U> geneSet = new HashSet<>();
        ArrayList<U> crossedOverGenes = new ArrayList<>();
        U currentGene = null;
        while (geneSet.size() != parent1.getGenotype().getGenes().size()) {
            if (currentGene == null) {
                if (remainingGenes.size() == 0) {
                    return mapCrossoverGenesToIndividuum(returnIndividuum, crossedOverGenes);
                }
                currentGene = getRandomGeneNotUsed(remainingGenes);
                crossedOverGenes.add(currentGene);
                geneSet.add(currentGene);
            }
            currentGene = getShortestEdgeGene(parent1, parent2, currentGene);
            if (geneSet.contains(currentGene)) {
                currentGene = null;
            } else if (currentGene != null) {
                geneSet.add(currentGene);
                crossedOverGenes.add(currentGene);
                remainingGenes.remove(currentGene);
            }
        }
        return mapCrossoverGenesToIndividuum(returnIndividuum, crossedOverGenes);
    }

    private <U extends IGene, T extends IGenotype<U>> U getShortestEdgeGene(Individuum<T, U> parent1, Individuum<T, U> parent2, U currentGene) {
        HashMap<Integer, U> indexCity = new HashMap<>();
        for (int i = 0; i < parent1.getGenotype().getGenes().size(); i++) {
            if (parent1.getGenotype().getGenes().get(i) == currentGene) {
                if (i + 1 > parent1.getGenotype().getGenes().size() / 2) {
                    if (i + 1 < parent1.getGenotype().getGenes().size()) {
                        indexCity.put(parent1.getGenotype().getGenes().size() - i - 1, parent1.getGenotype().getGenes().get(i + 1));
                    }
                } else {
                    if (i > 0) {
                        indexCity.put(i - 1, parent1.getGenotype().getGenes().get(i - 1));
                    }
                }
            }
            if (parent2.getGenotype().getGenes().get(i) == currentGene) {
                if (i + 1 > parent2.getGenotype().getGenes().size() / 2) {
                    if (i + 1 < parent1.getGenotype().getGenes().size()) {
                        indexCity.put(parent2.getGenotype().getGenes().size() - i - 1, parent2.getGenotype().getGenes().get(i + 1));
                    }
                } else {
                    if (i > 0)
                        indexCity.put(i - 1, parent2.getGenotype().getGenes().get(i - 1));
                }
            }
        }
        if (indexCity.keySet().isEmpty()) {
            return null;
        }
        int min = Collections.min(indexCity.keySet());
        return indexCity.get(min);
    }

    private <T extends IGenotype<U>, U extends IGene> U getRandomGeneNotUsed(ArrayList<U> remainingGenes) {
        int index = this.randomGenerator.nextInt(remainingGenes.size());
        U returnGene = remainingGenes.get(index);
        remainingGenes.remove(index);
        return returnGene;
    }


    private <T extends IGenotype<U>, U extends IGene> Individuum<T, U> mapCrossoverGenesToIndividuum(Individuum<T, U> returnIndividuum, ArrayList<U> crossedOverGenes) {
        for (int i = 0; i < crossedOverGenes.size(); i++) {
            returnIndividuum.getGenotype().getGenes().set(i, crossedOverGenes.get(i));
        }
        return returnIndividuum;
    }

    @Override
    protected <T extends IGenotype<U>, U extends IGene> ArrayList<Individuum<T, U>> executeInner(Individuum<T, U> parent1, Individuum<T, U> parent2) {
        if (parent1.getGenotype().getGenes().get(0).getClass() != parent2.getGenotype().getGenes().get(0).getClass())
            throw new RuntimeException("Crossover only works with parents that hold the same concrete type of gene!");

        ArrayList<Individuum<T, U>> children = new ArrayList<>();

        children.add(getChildWithCossover(parent1, parent2));
        Individuum<T, U> compareIndividuum = getChildWithCossover(parent1, parent2);
        while (children.get(0) == compareIndividuum) {
            compareIndividuum = getChildWithCossover(parent1, parent2);
        }
        children.add(compareIndividuum);
        return children;
    }
}
