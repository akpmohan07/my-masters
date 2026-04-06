package dev.mohanverse;

import dev.mohanverse.config.MatrixConfig;
import dev.mohanverse.config.ProcessorConfig;
import dev.mohanverse.config.RunContext;
import dev.mohanverse.multiplier.MatrixMultiplier;
import dev.mohanverse.multiplier.SequentialMultiplier;
import dev.mohanverse.utils.MatrixUtils;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Slf4j
public class MatrixBenchmark {
    private MatrixConfig matrixConfig;

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

            for(ProcessorConfig.CoreConfig coreConfig : matrixConfig.getProcessorConfig().getCoreConfigs()) {
                log.info("Running benchmark with {}% of {} / {} cores", coreConfig.getCorePercentage(),  coreConfig.getCoreCount(), matrixConfig.getProcessorConfig().getAvailableCores());
                MatrixMetrics matrixMetrics = new MatrixMetrics();
                RunContext runContext = new RunContext(matrixSize, coreConfig, matrixMetrics);
                runMultipliers(A, B, runContext);
            }
        }
    }

    private void runMultipliers(double[][] A, double[][] B, RunContext runContext) {
        for (MatrixMultiplier multiplier : matrixConfig.getMultipliers()) {
            log.info("Running benchmark with multiplier: {}", multiplier.getClass().getSimpleName());
            MatrixResult matrixResult = multiplier.multiply(A, B, runContext);
            MatrixUtils.printMatrix(matrixResult.getResultMatrix());
        }
    }

}
