package dev.mohanverse.utils;


import dev.mohanverse.config.enums.ExecutionType;
import dev.mohanverse.core.MatrixResult;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MatrixUtils {

    private MatrixUtils() {
            // Private constructor to prevent instantiation as this is a utility class
        }

        public static double[][] generateMatrix(int rows, int cols) {
            double[][] matrix = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    matrix[i][j] = Math.random();
                }
            }
            return matrix;
        }

    public static void printMatrix(double[][] matrix) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (double[] row : matrix) {
            for (double value : row) {
                sb.append(String.format("%.2f ", value));
            }
            sb.append("\n");
        }
        log.info(sb.toString());
    }

    public static boolean verifyResult(double[][] result, double[][] expected) {
        if (result.length != expected.length || result[0].length != expected[0].length) {
            log.error("Result and expected matrices have different dimensions");
            return false;
        }
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                if (Math.abs(result[i][j] - expected[i][j]) > 1e-6) { // Allowing a small tolerance for floating-point comparisons
                    log.error("Mismatch at position ({}, {}): result={}, expected={}", i, j, result[i][j], expected[i][j]);
                    return false;
                }
            }
        }
        return true;
    }

    public static double calculateSpeedUp(double sequentialTime, double parallelTime) {
        return sequentialTime / parallelTime;
    }

    public static double deriveSpeedUp(List<MatrixResult> results, MatrixResult matrixResult) {
        MatrixResult sequentialResult = results.stream()
                .filter(r -> r.getRunContext().executionType() == ExecutionType.SEQUENTIAL)
                .filter(r -> r.getRunContext().matrixSize() == matrixResult.getRunContext().matrixSize())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No sequential result found for speedup calculation"));

        return calculateSpeedUp(sequentialResult.getRunContext().metrics().getExecutionTimeMillis(), matrixResult.getRunContext().metrics().getExecutionTimeMillis());
    }
}
