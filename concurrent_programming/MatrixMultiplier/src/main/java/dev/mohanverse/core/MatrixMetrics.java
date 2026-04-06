package dev.mohanverse;


public class MatrixMetrics {
    private  long executionTimeMillis;
    private  long startTimeMillis;
    private  long endTimeMillis;
    private boolean verified;
    private double speedup;

    public void startTimer() {
        startTimeMillis = System.currentTimeMillis();
    }

    public void stopTimer() {
        endTimeMillis = System.currentTimeMillis();
        executionTimeMillis = endTimeMillis - startTimeMillis;
    }
}
