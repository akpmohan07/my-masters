package dev.mohanverse.prime.config;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ProcessorConfig {

    private final int availableCores;

    @Setter
    @Getter
    private List<ThreadConfig> threadConfigs;

    public ProcessorConfig() {
        this.availableCores = Runtime.getRuntime().availableProcessors();
    }

    @Getter
    public class ThreadConfig {
        private final int threadCount;
        private final int corePercentage;

        public ThreadConfig(int corePercentage) {
            this.threadCount = getThreadCountFromPercentage(corePercentage);
            this.corePercentage = corePercentage;
        }

        public ThreadConfig(int threadCount, int corePercentage) {
            this.threadCount = threadCount;
            this.corePercentage = corePercentage;
        }
    }

    // cores → percentage
    private int getPercentageFromThreadCount(int threadCount) {
        return (int) Math.round(threadCount * 100.0 / availableCores);
    }

    // percentage → cores
    private int getThreadCountFromPercentage(int percentage) {
        return Math.max(1, (int) Math.round(availableCores * percentage / 100.0));
    }

    public ThreadConfig getSingleThreadConfig() {
        return new ThreadConfig(1, getPercentageFromThreadCount(1));
    }
}
