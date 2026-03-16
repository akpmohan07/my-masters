package dev.mohanverse.prime.executor;

import dev.mohanverse.prime.config.PrimeFinderConfig;
import dev.mohanverse.prime.config.ProcessorConfig;
import dev.mohanverse.prime.finder.PrimeFinderAbstract;
import dev.mohanverse.prime.finder.trial_division.LinearTrialDivisionFinder;
import dev.mohanverse.prime.finder.PrimeFinderResult;
import dev.mohanverse.prime.finder.trial_division.ParallelTrialDivisionFinder;
import dev.mohanverse.prime.utility.ResultsPrinter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class PrimerFinderExecutor {
    private final long[] testRanges;

    public  PrimerFinderExecutor(long[] testRanges) {
        this.testRanges = testRanges;
    }

    public void execute() {
        ResultsPrinter.printHeader();
        LinearTrialDivisionFinder linearFinder = new LinearTrialDivisionFinder(
                PrimeFinderConfig.getProcessorConfig().getSingleThreadConfig());

        for (long range : testRanges) {
            try {
                runAndLog(linearFinder, range);
                for (ProcessorConfig.ThreadConfig config : PrimeFinderConfig.getProcessorConfig().getThreadConfigs()) {
                    runAndLog(new ParallelTrialDivisionFinder(config), range);
                }
                ResultsPrinter.printFooter();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void runAndLog(PrimeFinderAbstract finder, long range) throws ExecutionException, InterruptedException {
        PrimeFinderResult result = finder.findPrimesUntil(range);
        ResultsPrinter.printRow(range, result);
    }
}
