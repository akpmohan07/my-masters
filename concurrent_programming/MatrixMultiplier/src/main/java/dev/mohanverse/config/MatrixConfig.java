package dev.mohanverse.config;

import dev.mohanverse.multiplier.MatrixMultiplier;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

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

    private long L1CacheSize;

    @Getter
    private int blockSize;

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

            // Load L1 cache size from properties
            log.info("Loading L1 cache size from system ...");
            String configuredCacheSize = properties.getProperty("matrix.multiplier.system.cache.size");
            if (configuredCacheSize != null && !configuredCacheSize.isBlank()) {
                L1CacheSize = Long.parseLong(configuredCacheSize);
            } else {
                L1CacheSize = detectL1CacheSize();
            }
            log.info("Detected L1 Cache Size: {} KB", L1CacheSize / 1024);

            // Calculate the optimal block size based on L1 cache size (assuming double precision, 8 bytes per element)
            blockSize = Integer.highestOneBit((int) Math.sqrt(L1CacheSize / 2.0 / Double.BYTES));
            log.info("Calculated block size based on L1 cache: {}", blockSize);

        } catch (Exception e) {
            log.error("Error loading MatrixConfig: ", e);
        }
    }

    private long detectL1CacheSize() {
        SystemInfo si = new SystemInfo();
        return si.getHardware().getProcessor().getProcessorCaches().stream()
                .filter(c -> c.getLevel() == 1)
                .filter(c -> c.getType() == CentralProcessor.ProcessorCache.Type.DATA)
                .mapToLong(CentralProcessor.ProcessorCache::getCacheSize)
                .min()
                .orElse(65536L);
    }
}
