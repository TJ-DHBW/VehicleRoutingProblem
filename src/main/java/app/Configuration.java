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
    // TODO: Set the log path
    public final Path logPath = Path.of("");

    public final VRPMode vrpMode = VRPMode.CVRP;

    // TODO validate the rates
    /**
     * Rate of a crossover happening. Between 0.6 and 0.8
     */
    public final double crossoverRate = 0.7;
    /**
     * Rate of a mutation happening. Between 0.001 and 0.005
     */
    public final double mutationRate = 0.003;
    public final int matingSelectionSize = 16;
    public final int initialPopulationSize = 42;
    public final int maxGenerationCount = 420;
    public final int penaltyPer1Lateness = 10;

    public final CrossoverType crossoverType = CrossoverType.UPX;
    public final MutationType mutationType = MutationType.CIM;
    public final SelectionType selectionType = SelectionType.TOS;
}
