package evolution;

import java.util.ArrayList;

public interface IGenotype {
    ArrayList<IGene> getGenes();
    double calculateFitness();
}
