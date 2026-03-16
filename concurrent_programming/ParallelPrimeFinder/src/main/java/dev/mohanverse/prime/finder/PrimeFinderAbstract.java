package dev.mohanverse.prime.finder;

import dev.mohanverse.prime.config.ProcessorConfig;
import dev.mohanverse.prime.enums.Algorithm;
import dev.mohanverse.prime.enums.ExecutionType;
import dev.mohanverse.prime.metrics.PrimeFinderMetrics;

import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class PrimeFinderAbstract {
    protected PrimeFinderMetrics primeFinderMetrics = new PrimeFinderMetrics();
    protected ProcessorConfig.ThreadConfig threadConfig;

    public  PrimeFinderAbstract(ProcessorConfig.ThreadConfig threadConfig) {
        this.threadConfig = threadConfig;
    }

    public final PrimeFinderResult findPrimesUntil(long end) throws ExecutionException, InterruptedException {
        return findPrimesForRange(2L, end);
    }

    public final PrimeFinderResult findPrimesForRange(long start, long end) throws ExecutionException, InterruptedException {
        primeFinderMetrics.startTimer();
        List<Long> primeNumbers = computePrimesForRange(start, end);
        primeFinderMetrics.stopTimer();
        primeFinderMetrics.setTotalNumbersChecked(end - start);
        primeFinderMetrics.setTotalPrimesFound(primeNumbers.size());
        return new PrimeFinderResult(primeNumbers, primeFinderMetrics, getAlgorithmName().getDisplayName(), getExecutionType().getDisplayName(), threadConfig);
    }

    protected abstract List<Long> computePrimesForRange(long start, long end) throws ExecutionException, InterruptedException;

    protected abstract Algorithm getAlgorithmName();
    protected abstract ExecutionType getExecutionType();
}
