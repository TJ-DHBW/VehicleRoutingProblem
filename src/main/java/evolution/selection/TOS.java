package evolution.selection;

import evolution.IGene;
import evolution.IGenotype;
import evolution.Individuum;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class TOS extends SelectionStrategy {
    private final Random randomGenerator;

    public TOS(Random randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    @Override
    public <T extends IGenotype<U>, U extends IGene> ArrayList<Individuum<T, U>> select(ArrayList<Individuum<T, U>> selectionPool, int selectionSize) {
        ArrayList<Individuum<T, U>> matingPool = new ArrayList<>();

        if (selectionPool.size() < 1) return matingPool;
        LinkedList<Individuum<T, U>> remaining = new LinkedList<>(selectionPool);
        // tournamentSize k is rounded up, because no individuum should be discarded without comparing fitness
        int k = (int) Math.ceil((double) remaining.size() / selectionSize);

        boolean unevenMates = false;
        while (!remaining.isEmpty()) {
            int selectedIndex = this.randomGenerator.nextInt(remaining.size());
            Individuum<T, U> currentChampion = remaining.get(selectedIndex);
            remaining.remove(currentChampion);
            for (int i = 1; i < k; i++) {
                if (remaining.isEmpty()) break;

                selectedIndex = this.randomGenerator.nextInt(remaining.size());
                Individuum<T, U> selectedContender = remaining.get(selectedIndex);
                remaining.remove(selectedContender);

                if (selectedContender.getFitness() < currentChampion.getFitness()) {
                    currentChampion = selectedContender;
                }
            }

            if (remaining.isEmpty() && !unevenMates) continue;

            matingPool.add(currentChampion);
            unevenMates = !unevenMates;
        }

        return matingPool;
    }
}
