package dev.mohanverse.multiplier.sequential;

import dev.mohanverse.config.RunContext;
import dev.mohanverse.config.enums.AlgorithmType;
import dev.mohanverse.config.enums.ExecutionType;
import dev.mohanverse.multiplier.MatrixMultiplier;

public class SequentialMultiplier extends MatrixMultiplier {

    @Override
    public double[][] doMultiply(double[][] A, double[][] B, RunContext runContext) {
        runContext.metrics().setNoOfTasksCreated(1);
        int n = runContext.matrixSize();

        double[][] C = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0;
                for (int k = 0; k < n; k++) {
                    sum += A[i][k] * B[k][j];
                }
                C[i][j] = sum;
            }
        }

        return C;
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.NAIVE;
    }

    @Override
    public ExecutionType getExecutionType() {
        return ExecutionType.SEQUENTIAL;
    }
}
