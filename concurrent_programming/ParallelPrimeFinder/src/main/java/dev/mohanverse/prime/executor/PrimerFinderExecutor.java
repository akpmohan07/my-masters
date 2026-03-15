package dev.mohanverse.prime.executor;

import dev.mohanverse.prime.finder.LinearPrimerFinder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class PrimerFinderExecutor {

    private static final Logger log = LoggerFactory.getLogger(PrimerFinderExecutor.class);
    LinearPrimerFinder linearPrimerFinder = new LinearPrimerFinder();

     public void execute() {
        log.info("Starting the execution of PrimerFinderExecutor");
        log.info(" Finding prime numbers in the range of 1 to 100: " +  linearPrimerFinder.findPrimeForRange(1, 100));
     }

}
