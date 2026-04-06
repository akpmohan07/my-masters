package dev.mohanverse.core;

import dev.mohanverse.config.RunContext;
import lombok.Getter;

@Getter
public class MatrixResult {
    private final RunContext runContext;
    private final double[][] resultMatrix;

    public MatrixResult(RunContext runContext, double[][] resultMatrix) {
        this.runContext = runContext;
        this.resultMatrix = resultMatrix;
    }
}
