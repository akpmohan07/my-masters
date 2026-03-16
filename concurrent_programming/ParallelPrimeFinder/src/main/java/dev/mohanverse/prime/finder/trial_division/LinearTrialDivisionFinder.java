package dev.mohanverse.prime.finder.trial_division;

import dev.mohanverse.prime.utility.PrimeUtils;
import dev.mohanverse.prime.config.ProcessorConfig;
import dev.mohanverse.prime.enums.Algorithm;
import dev.mohanverse.prime.enums.ExecutionType;
import dev.mohanverse.prime.finder.PrimeFinderAbstract;

import java.util.List;

public class LinearTrialDivisionFinder extends PrimeFinderAbstract {

    public LinearTrialDivisionFinder(ProcessorConfig.ThreadConfig threadConfig) {
        super(threadConfig);
    }

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
