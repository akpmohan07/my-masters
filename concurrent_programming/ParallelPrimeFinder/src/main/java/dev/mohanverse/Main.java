package dev.mohanverse;

import dev.mohanverse.prime.config.PrimeFinderConfig;
import dev.mohanverse.prime.executor.PrimerFinderExecutor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
public class Main {
    public static void main(String[] args) throws IOException {
        log.info("Loading configuration...");
        PrimeFinderConfig.load();
        PrimerFinderExecutor executor = new PrimerFinderExecutor(PrimeFinderConfig.getTestValues());
        executor.execute();
    }
}