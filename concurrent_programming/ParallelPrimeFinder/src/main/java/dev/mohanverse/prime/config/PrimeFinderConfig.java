package dev.mohanverse.prime.config;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import static java.lang.Integer.parseInt;

public class PrimeFinderConfig {

    private static long[] testValues;
    ProcessorConfig processorConfig = new ProcessorConfig(Runtime.getRuntime().availableProcessors());


    private static final int availableCores =;

    // prevent instantiation
    private PrimeFinderConfig() {}

    public static void load() throws IOException {
        Properties props = new Properties();

        InputStream input =
                PrimeFinderConfig.class.getClassLoader()
                        .getResourceAsStream("config.properties");

        props.load(input);

        testValues =  Arrays.stream(
                props.getProperty("prime.test.values").split(","))
                .map(String::trim)
                .mapToLong(Long::parseLong)
                .toArray();

        processorConfig

        threadConfigs = Arrays.stream(
                props.getProperty("prime.thread.percentages").split(","))
                .map(String::trim)
                .mapToInt(p -> Math.max(1, (int) Math.round(availableCores * Integer.parseInt(p) / 100.0)))
                .toArray();
    }

    public static long[] getTestValues() { return testValues; }
    public static int[] getThreadCounts() { return threadCounts; }
}