package dev.mohanverse.report;

import dev.mohanverse.config.RunContext;
import dev.mohanverse.core.MatrixMetrics;
import dev.mohanverse.core.MatrixResult;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ReportPrinter {

    private static final String SEPARATOR =
            "+---------------------+------------+--------+---------+------------+---------+-----------+";
    private static final String HEADER = String.format(
            "| %-19s | %-10s | %-6s | %-7s | %-10s | %-7s | %-9s |",
            "Algorithm", "Type", "Size", "Threads", "Time (ms)", "Speedup", "Correct?"
    );

    public static  void printReport(List<MatrixResult> results) {
        log.info("");
        log.info("MATRIX MULTIPLICATION BENCHMARK RESULTS");
        log.info("{}", SEPARATOR);
        log.info("{}", HEADER);
        log.info("{}", SEPARATOR);
        for (MatrixResult result : results) {
            printRow(result);
        }
        log.info("{}", SEPARATOR);
//        printSummary(results);
    }

    private static void printRow(MatrixResult result) {
        MatrixMetrics metrics = result.getRunContext().metrics();
        RunContext runContext = result.getRunContext();
        log.info("{}", String.format(
                "| %-19s | %-10s | %-6d | %-7d | %-10d | %-7.2f | %-9s |",
                runContext.algorithmType(),
                runContext.executionType(),
                runContext.matrixSize(),
                runContext.coreConfig().getCoreCount(),
                metrics.getExecutionTimeMillis(),
                metrics.getSpeedup(),
                metrics.isVerified() ? "YES" : "NO"
        ));
    }

//    private void printSummary(List<MatrixResult> results) {
//        log.info("");
//        log.info("SUMMARY");
//        log.info("Total runs: {}", results.size());
//
//        results.stream()
//                .filter(r -> r.metrics().getSpeedup() > 0)
//                .max((a, b) -> Double.compare(
//                        a.metrics().getSpeedup(),
//                        b.metrics().getSpeedup()))
//                .ifPresent(best -> log.info(
//                        "Best speedup: {}x — {} {} threads={} size={}",
//                        String.format("%.2f", best.metrics().getSpeedup()),
//                        best.metrics().getRunContext().algorithmType(),
//                        best.metrics().getRunContext().executionType(),
//                        best.metrics().getRunContext().coreConfig().getCoreCount(),
//                        best.metrics().getRunContext().matrixSize()
//                ));
//    }
}