package dev.mohanverse.multiplier.parallel;

import dev.mohanverse.config.RunContext;
import dev.mohanverse.config.enums.AlgorithmType;
import dev.mohanverse.config.enums.ExecutionType;
import dev.mohanverse.multiplier.MatrixMultiplier;

public class BlockedParallelMultiplier extends MatrixMultiplier {
    @Override
    public double[][] doMultiply(double[][] A, double[][] B, RunContext runContext) {
        return new double[0][];
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.BLOCKED;
    }

    @Override
    public ExecutionType getExecutionType() {
        return ExecutionType.PARALLEL;
    }
}
