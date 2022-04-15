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
    public final double crossoverRate = 0.7;
    public final double mutationRate = 0.0003;
    public final int matingSelectionSize = 16;
    public final int initialPopulationSize = 42;
    public final int maxGenerationCount = 420;

    public final CrossoverType crossoverType = CrossoverType.UPX;
    public final MutationType mutationType = MutationType.CIM;
    public final SelectionType selectionType = SelectionType.TOS;
}
