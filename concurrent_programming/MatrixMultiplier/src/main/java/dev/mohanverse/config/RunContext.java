package dev.mohanverse.config;


import dev.mohanverse.config.enums.AlgorithmType;
import dev.mohanverse.config.enums.ExecutionType;
import dev.mohanverse.core.MatrixMetrics;
import lombok.Getter;

public record RunContext(int matrixSize, ProcessorConfig.CoreConfig coreConfig, MatrixMetrics metrics, AlgorithmType algorithmType, ExecutionType executionType) {
}
