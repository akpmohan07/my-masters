package dev.mohanverse.prime.finder.sieve;

import dev.mohanverse.prime.config.ProcessorConfig;
import dev.mohanverse.prime.enums.Algorithm;
import dev.mohanverse.prime.enums.ExecutionType;
import dev.mohanverse.prime.finder.PrimeFinderAbstract;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ParallelSieveFinder extends PrimeFinderAbstract {

    public ParallelSieveFinder(ProcessorConfig.ThreadConfig threadConfig) {
        super(threadConfig);
    }

    @Override
    protected List<Long> computePrimesForRange(long start, long end) throws ExecutionException, InterruptedException {

        // Step 1 — create shared array
        boolean[] isComposite = new boolean[(int) (end + 1)];
        isComposite[0] = true;
        isComposite[1] = true;

        // Step 2 — find base primes up to √N sequentially
        int limit = (int) Math.sqrt(end);
        List<Integer> basePrimes = new ArrayList<>();
        for (int p = 2; p <= limit; p++) {
            if (!isComposite[p]) {
                basePrimes.add(p);
                for (int j = p * p; j <= end; j += p) {
                    isComposite[j] = true;
                }
            }
        }

        // Step 3 — create ExecutorService
        ExecutorService executorService = Executors.newFixedThreadPool(threadConfig.getThreadCount(),
                r ->{
                    Thread t = new Thread(r);
                    t.setName("ParallelSieveFinder-Thread-" + t.getId());
                    return t;
                });

        // Step 4 — split range into chunks, each thread marks its segment
        long chunkSize = (end - start) / threadConfig.getThreadCount();
        primeFinderMetrics.setChunkSize(chunkSize);

        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < threadConfig.getThreadCount(); i++) {
            final long segStart = start + (long) i * chunkSize;
            final long segEnd = (i == threadConfig.getThreadCount() - 1)
                    ? end : segStart + chunkSize - 1;

            tasks.add(() -> {
                log.info("Thread {} working on [{}, {}]",
                        Thread.currentThread().getName(), segStart, segEnd);
                for (int p : basePrimes) {
                    long firstMultiple = ((segStart + p - 1) / p) * p;
                    if (firstMultiple == p) firstMultiple += p;
                    for (long j = firstMultiple; j <= segEnd; j += p) {
                        isComposite[(int) j] = true;
                    }
                }
                return null;
            });
        }

        // Step 5 — invoke all tasks
        executorService.invokeAll(tasks);

        // Step 6 — collect results
        List<Long> primes = new ArrayList<>();
        for (int i = (int) start; i <= end; i++) {
            if (!isComposite[i]) {
                primes.add((long) i);
            }
        }

        // Step 7 — shutdown
        executorService.shutdown();

        return primes;
    }

    @Override
    protected Algorithm getAlgorithmName() {
        return Algorithm.SIEVE_OF_ERATOSTHENES;
    }

    @Override
    protected ExecutionType getExecutionType() {
        return ExecutionType.PARALLEL;
    }
}