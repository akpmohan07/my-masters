package dev.mohanverse.prime.finder;

import dev.mohanverse.prime.PrimeUtils;
import dev.mohanverse.prime.enums.Algorithm;
import dev.mohanverse.prime.enums.ExecutionType;
import dev.mohanverse.prime.metrics.PrimeFinderMetrics;

import java.util.List;

public class LinearTrialDivisionFinder extends  PrimeFinderAbstract{

    @Override
    protected List<Long> computePrimesForRange(long start, long end) {
        List<Long> primeNumbers = new java.util.ArrayList<>();
        for (long i = start; i <= end; i++) {
            if (PrimeUtils.isPrime(i)) {
                primeNumbers.add(i);
            }
        }
        return primeNumbers;
    }

    @Override
    protected Algorithm getAlgorithmName() {
        return Algorithm.LINEAR_TRIAL_DIVISION;
    }

    @Override
    protected ExecutionType getExecutionType() {
        return ExecutionType.SEQUENTIAL;
    }
}
