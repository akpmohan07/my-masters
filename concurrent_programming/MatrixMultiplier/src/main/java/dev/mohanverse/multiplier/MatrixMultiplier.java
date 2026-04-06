package dev.mohanverse.multiplier;

import dev.mohanverse.core.MatrixResult;
import dev.mohanverse.config.RunContext;
import dev.mohanverse.config.enums.AlgorithmType;
import dev.mohanverse.config.enums.ExecutionType;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class MatrixMultiplier {

    public MatrixResult multiply(double[][] A, double[][] B, RunContext runContext) {
        runContext.metrics().startTimer();
        double[][] result =  doMultiply(A, B, runContext);
        runContext.metrics().stopTimer();
        return new MatrixResult(runContext, result);
    }

    public abstract double[][] doMultiply(double[][] A, double[][] B, RunContext runContext);

    public abstract AlgorithmType getAlgorithmType();
    public abstract ExecutionType getExecutionType();

}
