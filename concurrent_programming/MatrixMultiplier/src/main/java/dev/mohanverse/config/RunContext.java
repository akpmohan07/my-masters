package dev.mohanverse.config;


import dev.mohanverse.MatrixMetrics;

public record RunContext(int matrixSize, ProcessorConfig.CoreConfig processorConfig, MatrixMetrics metrics) {
}
