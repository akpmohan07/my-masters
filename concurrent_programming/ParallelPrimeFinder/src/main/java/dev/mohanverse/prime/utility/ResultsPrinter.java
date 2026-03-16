package dev.mohanverse.prime.utility;

import dev.mohanverse.prime.finder.PrimeFinderResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResultsPrinter {
    private static final String FORMAT = "%-15s | %-25s | %-12s | %-7s | %-6s | %-12s | %-15s | %s";
    private static final String SEPARATOR = "-".repeat(120);

    private ResultsPrinter() {}


    public static void printHeader() {
        log.info(SEPARATOR);
        log.info(String.format(FORMAT,
                "N", "Algorithm", "Type", "Threads", "Core%",
                "Time", "Chunk Size", "Primes"));
        log.info(SEPARATOR);
    }

    public static void printRow(long n, PrimeFinderResult result) {
        log.info(String.format(FORMAT,
                String.format("%,d", n),
                result.getAlgorithm(),
                result.getExecutionType(),
                result.getThreadConfig().getThreadCount(),
                result.getThreadConfig().getCorePercentage() + "%",
                formatTime(result.getPrimeFinderMetrics().getExecutionTimeInMillis()),
                String.format("%,d", result.getPrimeFinderMetrics().getChunkSize()),
                String.format("%,d", result.getPrimeFinderMetrics().getTotalPrimesFound())
        ));
    }

    private static String formatTime(long ms) {
        if (ms < 1000) {
            return ms + " ms";
        } else if (ms < 60000) {
            return String.format("%.2f sec", ms / 1000.0);
        } else {
            return String.format("%.2f min", ms / 60000.0);
        }
    }


    public static void printFooter() {
        log.info(SEPARATOR);
    }
}