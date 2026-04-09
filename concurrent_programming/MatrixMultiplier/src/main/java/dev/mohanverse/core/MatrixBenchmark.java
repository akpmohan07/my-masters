package dev.mohanverse.core;

import dev.mohanverse.config.MatrixConfig;
import dev.mohanverse.config.ProcessorConfig;
import dev.mohanverse.config.RunContext;
import dev.mohanverse.multiplier.MatrixMultiplier;
import dev.mohanverse.multiplier.sequential.SequentialMultiplier;
import dev.mohanverse.report.ReportPrinter;
import dev.mohanverse.utils.MatrixUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.SequencedCollection;

@Slf4j
public class MatrixBenchmark {
    private MatrixConfig matrixConfig;
    private List<MatrixResult> results = new java.util.ArrayList<>();

    public MatrixBenchmark () {
        setup();
    }

    private void setup() {
        loadConfigurations();

    }

    private void loadConfigurations() {
        matrixConfig = new MatrixConfig();
    }

    public void run() {
        for (int matrixSize : matrixConfig.getMatrixSizes()) {
            log.info("Running benchmark for matrix size: {}x{}", matrixSize, matrixSize);
            // Here you would call your matrix multiplication methods and measure their performance
            double [][] A = MatrixUtils.generateMatrix(matrixSize, matrixSize);
            double [][] B = MatrixUtils.generateMatrix(matrixSize, matrixSize);

            double[][] expectedResult = runBaseSequentialMultiplier(A, B, matrixSize, matrixConfig.getProcessorConfig().getSingleThreadConfig()).getResultMatrix();

            for(ProcessorConfig.CoreConfig coreConfig : matrixConfig.getProcessorConfig().getCoreConfigs()) {
                log.info("Running benchmark with {}% of {} / {} cores", coreConfig.getCorePercentage(),  coreConfig.getCoreCount(), matrixConfig.getProcessorConfig().getAvailableCores());
                runParallelMultipliers(A, B, matrixSize, coreConfig, expectedResult);
            }
        }
        ReportPrinter.printReport(results);
    }

    private MatrixResult runBaseSequentialMultiplier(double[][] A, double[][] B, int matrixSize, ProcessorConfig.CoreConfig coreConfig) {
        SequentialMultiplier sequentialMultiplier = new SequentialMultiplier();
        RunContext runContext = new RunContext(matrixSize, coreConfig, new MatrixMetrics(), sequentialMultiplier.getAlgorithmType(), sequentialMultiplier.getExecutionType(), matrixConfig.getBlockSize());
        log.info("Running benchmark with multiplier: {}", sequentialMultiplier.getClass().getSimpleName());
        MatrixResult matrixResult = sequentialMultiplier.multiply(A, B, runContext);
        matrixResult.getRunContext().metrics().setVerified(true);
        matrixResult.getRunContext().metrics().setSpeedup(1.0);
        results.add(matrixResult);
        return matrixResult;
    }

    private void runParallelMultipliers(double[][] A, double[][] B, int matrixSize,  ProcessorConfig.CoreConfig coreConfig, double[][] expectedResult) {
        for (MatrixMultiplier multiplier : matrixConfig.getMultipliers()) {
            RunContext runContext = new RunContext(matrixSize, coreConfig, new MatrixMetrics(), multiplier.getAlgorithmType(), multiplier.getExecutionType(), matrixConfig.getBlockSize());
            log.info("Running benchmark with multiplier: {}", multiplier.getClass().getSimpleName());
            MatrixResult matrixResult = multiplier.multiply(A, B, runContext);
            matrixResult.getRunContext().metrics().setVerified(MatrixUtils.verifyResult(matrixResult.getResultMatrix(), expectedResult));
            matrixResult.getRunContext().metrics().setSpeedup(MatrixUtils.deriveSpeedUp(results, matrixResult));
            results.add(matrixResult);
        }
    }

}
