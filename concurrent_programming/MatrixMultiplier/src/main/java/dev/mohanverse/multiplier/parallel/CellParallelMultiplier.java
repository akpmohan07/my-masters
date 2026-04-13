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
public class CellParallelMultiplier extends MatrixMultiplier {
    @Override
    public double[][] doMultiply(double[][] A, double[][] B, RunContext runContext) {
        log.info("Starting Cell Parallel Multiplication with {} cores", runContext.coreConfig().getCoreCount());

        int m = A.length;
        int n = A[0].length;
        int p = B[0].length;

        double[][] result = new double[m][p];
        List<CellMultiplierTask> tasks = new ArrayList<>();

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < p; j++) {
                CellMultiplierTask task = new CellMultiplierTask(i, j, A, B, result);
                tasks.add(task);
            }
        }

        log.info("Created {} tasks for cell multiplication", tasks.size());
        runContext.metrics().setNoOfTasksCreated(tasks.size());

        try (ForkJoinPool pool = new ForkJoinPool(runContext.coreConfig().getCoreCount())) {
            log.info("Invoking tasks in ForkJoinPool");
            pool.submit(() -> ForkJoinTask.invokeAll(tasks)).join();
            log.info("Completed Cell Parallel Multiplication");
        }
        return result;
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.CELL;
    }

    @Override
    public ExecutionType getExecutionType() {
       return ExecutionType.PARALLEL;
    }

    public static
    class CellMultiplierTask extends RecursiveAction {
        private final int row;
        private final int col;
        private final double[][] A;
        private final double[][] B;
        private final double[][] result;

        public CellMultiplierTask(int row, int col, double[][] A, double[][] B, double[][] result) {
            this.row = row;
            this.col = col;
            this.A = A;
            this.B = B;
            this.result = result;
        }


        @Override
        protected void compute() {
            double sum = 0;
            for(int k = 0; k < A[0].length; k++) {
                sum += A[row][k] * B[k][col];
            }
            result[row][col] = sum;
        }
    }
}
