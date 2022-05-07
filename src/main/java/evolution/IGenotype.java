package evolution;

import java.util.ArrayList;

public interface IGenotype<T extends IGene> {
    ArrayList<T> getGenes();

    IGenotype<T> createCopy();

    boolean equals(Object obj);
}
