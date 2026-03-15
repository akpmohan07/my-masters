package dev.mohanverse.prime.finder;

import dev.mohanverse.prime.enums.Algorithm;
import dev.mohanverse.prime.enums.ExecutionType;
import dev.mohanverse.prime.metrics.PrimeFinderMetrics;

import java.util.List;

public abstract class PrimeFinderAbstract {
    protected PrimeFinderMetrics primeFinderMetrics = new PrimeFinderMetrics();

    public final PrimeFinderResult findPrimesUntil(long end) {
        return findPrimesForRange(2L, end);
    }

    protected final PrimeFinderResult findPrimesForRange(long start, long end) {
        primeFinderMetrics.startTimer();
        List<Long> primeNumbers = computePrimesForRange(start, end);
        primeFinderMetrics.stopTimer();
        primeFinderMetrics.setTotalNumbersChecked(end - start);
        primeFinderMetrics.setTotalPrimesFound(primeNumbers.size());
        return new PrimeFinderResult(primeNumbers, primeFinderMetrics, getAlgorithmName().getDisplayName(), getExecutionType().getDisplayName());
    }

    protected abstract List<Long> computePrimesForRange(long start, long end);

    protected abstract Algorithm getAlgorithmName();
    protected abstract ExecutionType getExecutionType();
}
