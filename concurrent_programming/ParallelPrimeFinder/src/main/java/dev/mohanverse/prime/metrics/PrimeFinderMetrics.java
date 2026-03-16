package dev.mohanverse.prime.metrics;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicLong;

@Getter
public class PrimeFinderMetrics {
    @Setter
    private long totalNumbersChecked;
    @Setter
    private long  totalPrimesFound;
    private long  executionTimeInMillis;
    private long  startTimeInMillis;
    private long  endTimeInMillis;
    private AtomicLong totalDivisibilityChecks;

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
