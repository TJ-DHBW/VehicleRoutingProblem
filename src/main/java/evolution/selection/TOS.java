package evolution.selection;

import evolution.Individuum;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class TOS extends SelectionStrategy{
    private final Random randomGenerator;

    public TOS(Random randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    @Override
    public ArrayList<Individuum<?, ?>> select(ArrayList<Individuum<?, ?>> selectionPool, int selectionSize) {
        ArrayList<Individuum<?, ?>> matingPool = new ArrayList<>();

        if (selectionPool.size() < 1) return matingPool;
        LinkedList<Individuum<?, ?>> remaining = new LinkedList<>(selectionPool);
        // tournamentSize k is rounded up, because no individuum should be discarded without comparing fitness
        int k = (int) Math.ceil((double)remaining.size()/selectionSize);

        boolean unevenMates = false;
        while (!remaining.isEmpty()){
            int selectedIndex = this.randomGenerator.nextInt(remaining.size());
            Individuum<?, ?> currentChampion = remaining.get(selectedIndex);
            remaining.remove(currentChampion);
            for (int i = 1; i < k; i++) {
                if(remaining.isEmpty()) break;

                selectedIndex = this.randomGenerator.nextInt(remaining.size());
                Individuum<?, ?> selectedContender = remaining.get(selectedIndex);
                remaining.remove(selectedContender);

                if (selectedContender.getFitness() < currentChampion.getFitness()){
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
