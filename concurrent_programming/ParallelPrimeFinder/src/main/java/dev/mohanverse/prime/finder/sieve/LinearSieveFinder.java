package dev.mohanverse.prime.finder.sieve;

import dev.mohanverse.prime.config.ProcessorConfig;
import dev.mohanverse.prime.enums.Algorithm;
import dev.mohanverse.prime.enums.ExecutionType;
import dev.mohanverse.prime.finder.PrimeFinderAbstract;

import java.util.ArrayList;
import java.util.List;

public class LinearSieveFinder extends PrimeFinderAbstract {

    public LinearSieveFinder(ProcessorConfig.ThreadConfig threadConfig) {
        super(threadConfig);
    }

    @Override
    protected List<Long> computePrimesForRange(long start, long end) {
        primeFinderMetrics.setChunkSize(end - start);
        boolean[] isComposite = new boolean[(int) (end + 1)];
        isComposite[0] = true;
        isComposite[1] = true;

        for (int p = 2; p <= Math.sqrt(end); p++) {
            if (!isComposite[p]) {
                for (int j = p * p; j <= end; j += p) {
                    isComposite[j] = true;
                }
            }
        }

        List<Long> primes = new ArrayList<>();
        for (int i = (int) start; i <= end; i++) {
            if (!isComposite[i]) {
                primes.add((long) i);
            }
        }
        return primes;
    }

    @Override
    protected Algorithm getAlgorithmName() {
        return Algorithm.SIEVE_OF_ERATOSTHENES;
    }

    @Override
    protected ExecutionType getExecutionType() {
        return ExecutionType.SEQUENTIAL;
    }
}