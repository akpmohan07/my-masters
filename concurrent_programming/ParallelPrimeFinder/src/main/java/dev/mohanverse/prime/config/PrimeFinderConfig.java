package dev.mohanverse.prime.config;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import static java.lang.Integer.parseInt;

public class PrimeFinderConfig {

    private static long[] testValues;
    @Getter
    private static  ProcessorConfig processorConfig;
    ;

    // prevent instantiation
    private PrimeFinderConfig() {}

    public static void load() throws IOException {
        processorConfig = new ProcessorConfig();
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


        processorConfig.setThreadConfigs(
                Arrays.stream(props.getProperty("prime.thread.percentages").split(","))
                        .map(String::trim)
                        .map(p -> processorConfig.new ThreadConfig(Integer.parseInt(p)))
                        .toList()
        );
    }

    public static long[] getTestValues() { return testValues; }
}