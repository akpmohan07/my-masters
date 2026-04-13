package dev.mohanverse.core;

import lombok.Getter;
import lombok.Setter;

@Getter
public class MatrixMetrics {
    private  long executionTimeMillis;
    private  long startTimeMillis;
    private  long endTimeMillis;
    @Setter
    private boolean verified;
    @Setter
    private double speedup;

    @Setter
    private long noOfTasksCreated;

    public void startTimer() {
        startTimeMillis = System.currentTimeMillis();
    }

    public void stopTimer() {
        endTimeMillis = System.currentTimeMillis();
        executionTimeMillis = endTimeMillis - startTimeMillis;
    }
}
