package app;

import evolution.crossover.CrossoverType;
import evolution.mutation.MutationType;
import evolution.selection.SelectionType;
import random.MersenneTwisterFast;

import java.util.Random;

public enum Configuration {
    INSTANCE;

    public final Random randomGenerator = new MersenneTwisterFast(System.nanoTime());

    // TODO validate the rates
    public final double crossoverRate = 0.7;
    public final double mutationRate = 0.0003;
    public final int populationSize = 42;

    public final CrossoverType crossoverType = CrossoverType.HRX;
    public final MutationType mutationType = MutationType.CIM;
    public final SelectionType selectionType = SelectionType.TOS;
}
