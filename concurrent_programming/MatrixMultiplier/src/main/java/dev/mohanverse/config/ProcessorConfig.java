package dev.mohanverse.config;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ProcessorConfig {
    @Getter
    private final int availableCores;
    @Setter
    @Getter
    List<CoreConfig> coreConfigs;

    public ProcessorConfig() {
        this.availableCores = Runtime.getRuntime().availableProcessors();
    }

    @Getter
    public class CoreConfig {
        private final int coreCount;
        private final int corePercentage;

        public CoreConfig(int corePercentage) {
            this.coreCount = getCoreCountFromPercentage(corePercentage);
            this.corePercentage = corePercentage;
        }

        public CoreConfig(int corePercentage, int percentageFromThreadCount) {
            this.coreCount = corePercentage;
            this.corePercentage = percentageFromThreadCount;
        }

        // percentage → cores
        private int getCoreCountFromPercentage(int percentage) {
            return Math.max(1, (int) Math.round(availableCores * percentage / 100.0));
        }



    }

    // cores → percentage
    private int getPercentageFromThreadCount(int coreCount) {
        return (int) Math.round(coreCount * 100.0 / availableCores);
    }

    public CoreConfig getSingleThreadConfig() {
        return new CoreConfig(1, getPercentageFromThreadCount(1));
    }
}
