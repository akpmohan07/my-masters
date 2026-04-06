package dev.mohanverse.config;

import dev.mohanverse.multiplier.MatrixMultiplier;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Slf4j
public class MatrixConfig {

    @Getter
    private int[] matrixSizes;
    @Getter
    private ProcessorConfig processorConfig;
    @Getter
    private List<? extends MatrixMultiplier> multipliers;

    public MatrixConfig(){
        log.info("Loading MatrixConfig...");
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("config.properties"));

            // Load matrix sizes from properties
            log.info("Loading matrix sizes from configuration...");
            matrixSizes = Arrays.stream(properties.getProperty("matrix.test.sizes").split(","))
                    .map(String::trim)
                    .mapToInt(Integer::parseInt)
                    .toArray();

            // Load ProcessorConfig from properties
            log.info("Loading processor configuration...");

            processorConfig = new ProcessorConfig();

            processorConfig.setCoreConfigs(
                    Arrays.stream(properties.getProperty("matrix.thread.percentages").split(","))
                            .map(String::trim)
                            .map(Integer::parseInt)
                            .map(p -> processorConfig.new CoreConfig(p))
                            .toList()
            );

            // Load multipliers from properties
            log.info("Loading matrix multipliers from configuration...");

            multipliers = new Reflections(properties.getProperty("matrix.multiplier.path").trim())
                    .getSubTypesOf(MatrixMultiplier.class)
                    .stream()
                    .map(clazz -> {
                        try {
                            return clazz.getDeclaredConstructor().newInstance();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();


        } catch (Exception e) {
            log.error("Error loading MatrixConfig: ", e);
        }

    }
}
