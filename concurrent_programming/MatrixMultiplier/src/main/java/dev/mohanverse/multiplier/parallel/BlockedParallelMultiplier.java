package dev.mohanverse.multiplier.parallel;

import dev.mohanverse.config.RunContext;
import dev.mohanverse.config.enums.AlgorithmType;
import dev.mohanverse.config.enums.ExecutionType;
import dev.mohanverse.multiplier.MatrixMultiplier;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

@Slf4j
public class BlockedParallelMultiplier extends MatrixMultiplier {
    @Override
    public double[][] doMultiply(double[][] A, double[][] B, RunContext runContext) {
        log.info("Starting Blocked Parallel Multiplication with {} cores and block size {}",
                runContext.coreConfig().getCoreCount(), runContext.blockSize());
        int n = runContext.matrixSize();
        double[][] C = new double[n][n];

        List<BlockMultiplierTask> tasks = new ArrayList<>();

        for (int i_block = 0; i_block < n; i_block += runContext.blockSize()) {
            for(int j_block = 0; j_block < n ; j_block += runContext.blockSize()) {
                int rowEnd = Math.min(i_block + runContext.blockSize(), n);
                int colEnd = Math.min(j_block + runContext.blockSize(), n);
                BlockMultiplierTask blockTask = new BlockMultiplierTask(i_block, rowEnd, j_block, colEnd, A, B, C, runContext.blockSize(), n);
                tasks.add(blockTask);
            }
        }

        log.info("Created {} tasks for blocked multiplication", tasks.size());

        try (ForkJoinPool pool = new ForkJoinPool(runContext.coreConfig().getCoreCount())) {
            log.info("Invoking tasks in ForkJoinPool");
            pool.submit(() -> ForkJoinTask.invokeAll(tasks)).join();
            log.info("Completed Blocked Parallel Multiplication");
        }
        return C;
    }

    public static class BlockMultiplierTask extends RecursiveAction {
        private final int rowStart;
        private final int rowEnd;
        private final int colStart;
        private final int colEnd;
        private final double[][] A;
        private final double[][] B;
        private final double[][] C;
        private final int blockSize;
        private final int n;

        public BlockMultiplierTask(int rowStart, int rowEnd, int colStart, int colEnd,
                                   double[][] A, double[][] B, double[][] C, int blockSize, int n) {
            this.rowStart = rowStart;
            this.rowEnd = rowEnd;
            this.colStart = colStart;
            this.colEnd = colEnd;
            this.A = A;
            this.B = B;
            this.C = C;
            this.blockSize = blockSize;
            this.n = n;
        }

        @Override
        protected void compute() {
            for (int k_block = 0; k_block < n; k_block += blockSize) {
                int blockColEnd = Math.min(k_block + blockSize, n);
                for (int i = rowStart; i < rowEnd; i++) {
                    for (int j = colStart; j < colEnd; j++) {
                        double sum = 0;
                        for (int k = k_block; k < blockColEnd; k++) {
                            sum += A[i][k] * B[k][j];
                        }
                        C[i][j] += sum;
                    }
                }
            }
        }
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
