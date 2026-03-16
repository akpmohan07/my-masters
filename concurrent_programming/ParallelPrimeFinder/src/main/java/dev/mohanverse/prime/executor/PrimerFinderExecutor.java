package dev.mohanverse.prime.executor;

import dev.mohanverse.prime.config.PrimeFinderConfig;
import dev.mohanverse.prime.config.ProcessorConfig;
import dev.mohanverse.prime.finder.PrimeFinderAbstract;
import dev.mohanverse.prime.finder.sieve.LinearSieveFinder;
import dev.mohanverse.prime.finder.sieve.ParallelSieveFinder;
import dev.mohanverse.prime.finder.trial_division.LinearTrialDivisionFinder;
import dev.mohanverse.prime.finder.PrimeFinderResult;
import dev.mohanverse.prime.finder.trial_division.ParallelTrialDivisionFinder;
import dev.mohanverse.prime.utility.ResultsPrinter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class PrimerFinderExecutor {
    private final long[] testRanges;

    public PrimerFinderExecutor(long[] testRanges) {
        this.testRanges = testRanges;
    }

    public void execute() {
        log.info("Starting the execution of PrimerFinderExecutor");

        LinearTrialDivisionFinder linearTrialFinder =
                new LinearTrialDivisionFinder(
                        PrimeFinderConfig.getProcessorConfig().getSingleThreadConfig());

        LinearSieveFinder linearSieveFinder =
                new LinearSieveFinder(
                        PrimeFinderConfig.getProcessorConfig().getSingleThreadConfig());

        ResultsPrinter.printHeader();

        for (long range : testRanges) {

            // Sequential Trial Division
            runAndLog(linearTrialFinder, range);

            // Sequential Sieve
            runAndLog(linearSieveFinder, range);

            // Parallel Trial Division — one run per thread config
            for (ProcessorConfig.ThreadConfig config :
                    PrimeFinderConfig.getProcessorConfig().getThreadConfigs()) {
                runAndLog(new ParallelTrialDivisionFinder(config), range);
            }

            for (ProcessorConfig.ThreadConfig config :
                    PrimeFinderConfig.getProcessorConfig().getThreadConfigs()) {
                runAndLog(new ParallelSieveFinder(config), range);
            }

            ResultsPrinter.printFooter();

        }

        log.info("Completed the execution of PrimerFinderExecutor");
    }

    private void runAndLog(PrimeFinderAbstract finder, long range) {
        try {
            PrimeFinderResult result = finder.findPrimesUntil(range);
            ResultsPrinter.printRow(range, result);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error finding primes up to {}", range, e);
            throw new RuntimeException(e);
        }
    }
}