package dev.mohanverse.prime.finder;

import dev.mohanverse.prime.config.ProcessorConfig;
import dev.mohanverse.prime.metrics.PrimeFinderMetrics;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PrimeFinderResult {
    private final List<Long> primeNumbers;
    private final PrimeFinderMetrics primeFinderMetrics;
    private final String algorithm;      // "Trial Division" or "Sieve"
    private final String executionType;  // "Sequential" or "Parallel"
    private final ProcessorConfig.ThreadConfig threadConfig;
}
