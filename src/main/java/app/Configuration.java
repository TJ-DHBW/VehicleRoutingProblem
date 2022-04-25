package app;

import evolution.crossover.CrossoverType;
import evolution.mutation.MutationType;
import evolution.selection.SelectionType;
import random.MersenneTwisterFast;
import vrp.VRPMode;

import java.nio.file.Path;
import java.util.Random;

public enum Configuration {
    INSTANCE;

    public final Random randomGenerator = new MersenneTwisterFast(System.nanoTime());

    public final String dataName = "data.txt";
    public final Path logPath = Path.of("./log.txt");

    public final VRPMode vrpMode = VRPMode.CVRP;
    public final boolean continuousFitnessValues = true;

    /**
     * Rate of a crossover happening. Between 0.6 and 0.8
     */
    public final double crossoverRate = 0.7;
    /**
     * Rate of a mutation happening. Between 0.001 and 0.005
     */
    public final double mutationRate = 0.003;
    public final int matingSelectionSize = 200;
    public final int initialPopulationSize = 420;
    public final int maxGenerationCount = 10000;
    public final int penaltyPer1Lateness = 10;

    public final CrossoverType crossoverType = CrossoverType.HRX;
    public final MutationType mutationType = MutationType.SCM;
    public final SelectionType selectionType = SelectionType.RWS;
}
