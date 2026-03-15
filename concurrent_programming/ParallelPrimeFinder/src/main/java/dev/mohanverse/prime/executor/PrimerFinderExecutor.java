package dev.mohanverse.prime.executor;

import dev.mohanverse.prime.finder.LinearTrialDivisionFinder;
import dev.mohanverse.prime.finder.PrimeFinderResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class PrimerFinderExecutor {
    private final long[] testRanges;

    public  PrimerFinderExecutor(long[] testRanges) {
        this.testRanges = testRanges;
    }


     public void execute() {
         log.info("Starting the execution of PrimerFinderExecutor");
         LinearTrialDivisionFinder linearTrialDivisionFinder = new LinearTrialDivisionFinder();

         for (long range : testRanges) {
             log.info(" Finding prime numbers in the range of 1 to " + range );
             PrimeFinderResult result = linearTrialDivisionFinder.findPrimesUntil(range);

             log.info("Algorithm: " + result.getAlgorithm());
             log.info("Execution Type: " + result.getExecutionType());
             log.info("Total numbers checked: " + result.getPrimeFinderMetrics().getTotalNumbersChecked());
             log.info("Total primes found: " + result.getPrimeFinderMetrics().getTotalPrimesFound());
             log.info("Time taken (ms): " + result.getPrimeFinderMetrics().getExecutionTimeInMillis());
//             log.info("Prime numbers found: " + result.getPrimeNumbers());

             log.info("--------------------------------------------------");

         }
    }
}
