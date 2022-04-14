package evolution;

import app.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class Population {
    // TODO: Maybe this will be final? Depends on how it is used by the genetic algorithm. Could be nice because you can set an enforced size.
    private final ArrayList<Individuum<?, ?>> individuums;

    public <U extends IGene, T extends IGenotype<U>> Population(Collection<Individuum<T, U>> individuums) {
        this.individuums = new ArrayList<>(individuums);
    }

    private void sort(){
        individuums.sort(Comparator.comparing(Individuum::getFitness));
    }

    public void exterminateStragglers(int numToExterminate){
        if (this.individuums.size() < numToExterminate) throw new RuntimeException("Can not exterminate "+numToExterminate+" individuals from a population of size "+this.individuums.size()+".");
        this.sort();

        for (int i = 0; i < numToExterminate; i++) {
            // FIXME: I dont like that we use the Configuration here.
            double randomNum = Configuration.INSTANCE.randomGenerator.nextDouble();
            int indexToExterminate = (int) ((this.individuums.size()-1) - (randomNum * numToExterminate));

            if (indexToExterminate < 0) {
                this.individuums.remove(this.individuums.size()-1);
                return;
            }
            this.individuums.remove(indexToExterminate);
        }
    }

    public ArrayList<Individuum<?, ?>> getIndividuums() {
        return individuums;
    }
}
