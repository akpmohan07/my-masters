package dev.mohanverse.prime.finder.trial_division;

import dev.mohanverse.prime.utility.PrimeUtils;
import dev.mohanverse.prime.config.ProcessorConfig;
import dev.mohanverse.prime.enums.Algorithm;
import dev.mohanverse.prime.enums.ExecutionType;
import dev.mohanverse.prime.finder.PrimeFinderAbstract;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class ParallelTrialDivisionFinder extends PrimeFinderAbstract {

    public ParallelTrialDivisionFinder(ProcessorConfig.ThreadConfig threadConfig) {
        super(threadConfig);
    }

    @Override
    protected List<Long> computePrimesForRange(long start, long end) throws ExecutionException, InterruptedException {
        // Step 1 — create ExecutorService with threadCount threads
        ExecutorService executorService = Executors.newFixedThreadPool(threadConfig.getThreadCount(),
        r -> {
            Thread t = new Thread(r);
            t.setName("ParallelTrialDivisionFinder-Thread-" + t.getId());
            return t;
        });

        // Step 2 — calculate chunk size
        long chunkSize = Math.round((float) (end - start) / threadConfig.getThreadCount());
        primeFinderMetrics.setChunkSize(chunkSize);

        // Step 3 — create Callable tasks
        List<Callable<List<Long>>> tasks = new ArrayList<>();
        for (int i = 0; i < threadConfig.getThreadCount(); i++) {
            final long chunkStart = start + i * chunkSize;
            final long chunkEnd = (i == threadConfig.getThreadCount() - 1) ? end : chunkStart + chunkSize - 1;
            tasks.add(() -> {
                log.info("Thread {} working on [{}, {}]",
                        Thread.currentThread().getName(), chunkStart, chunkEnd);
                List<Long> localPrimes = new ArrayList<>();
                for (long j = chunkStart; j <= chunkEnd; j++) {
                    if (PrimeUtils.isPrime(j)) {
                        localPrimes.add(j);
                    }
                }
                return localPrimes;
            });
        }

        // Step 4 — invoke all tasks
        List<Future<List<Long>>> futures = executorService.invokeAll(tasks);

        // Step 5 — merge results
        List<Long> allPrimes = new ArrayList<>();
        for (Future<List<Long>> future : futures) {
            allPrimes.addAll(future.get());
        }

        // Step 6 — shutdown ExecutorService
        executorService.shutdown();

        return allPrimes;
    }



    @Override
    protected Algorithm getAlgorithmName() {
        return Algorithm.LINEAR_TRIAL_DIVISION;
    }

    @Override
    protected ExecutionType getExecutionType() {
        return ExecutionType.PARALLEL;
    }
}