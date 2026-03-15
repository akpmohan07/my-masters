package dev.mohanverse.prime.metrics;

public class PrimeFinderMetrics {
    Integer totalNumbersChecked;
    Integer totalPrimesFound;
    Long executionTimeInMillis;
    Long startTimeInMillis;
    Long endTimeInMillis;

    public void startTimer() {
        this.startTimeInMillis = System.currentTimeMillis();
    }

    public void stopTimer() {
        this.endTimeInMillis = System.currentTimeMillis();
        this.executionTimeInMillis = endTimeInMillis - startTimeInMillis;
    }

    public void incrementTotalNumbersChecked() {
        this.totalNumbersChecked++;
    }

    public void incrementTotalPrimesFound() {
        this.totalPrimesFound++;
    }
}
