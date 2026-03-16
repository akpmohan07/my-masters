package dev.mohanverse.prime.utility;

import dev.mohanverse.prime.finder.PrimeFinderResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResultsPrinter {

    private static final String FORMAT = "%-15s | %-25s | %-12s | %-7s | %-6s | %-9s | %s";
    private static final String SEPARATOR = "-".repeat(100);

    private ResultsPrinter() {}

    public static void printHeader() {
        log.info(SEPARATOR);
        log.info(String.format(FORMAT, "N", "Algorithm", "Type", "Threads", "Core%", "Time(ms)", "Primes"));
        log.info(SEPARATOR);
    }

    public static void printRow(long n, PrimeFinderResult result) {
        log.info(String.format(FORMAT,
                String.format("%,d", n),
                result.getAlgorithm(),
                result.getExecutionType(),
                result.getThreadConfig().getThreadCount(),
                result.getThreadConfig().getCorePercentage() + "%",
                result.getPrimeFinderMetrics().getExecutionTimeInMillis(),
                String.format("%,d", result.getPrimeFinderMetrics().getTotalPrimesFound())
        ));
    }

    public static void printFooter() {
        log.info(SEPARATOR);
    }
}