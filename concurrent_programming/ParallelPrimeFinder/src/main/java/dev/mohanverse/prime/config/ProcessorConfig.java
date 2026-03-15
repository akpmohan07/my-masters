package dev.mohanverse.prime.config;

public class ProcessorConfig {

        private static final int availableCores;

        public ProcessorConfig(int processorCount) {
            this.availableCores = processorCount;
        }

    public class ThreadConfig {
        private final int threadCount;
        private final int corePercentage;

        public ThreadConfig(int threadCount) {
            this.threadCount = threadCount;
            this.corePercentage = getCorePercentage(threadCount);
        }
    }

    // cores → percentage
    public int getCorePercentage(int threadCount) {
        return (int) Math.round(threadCount * 100.0 / availableCores);
    }

    // percentage → cores
    public int getThreadCount(int percentage) {
        return Math.max(1, (int) Math.round(availableCores * percentage / 100.0));
    }
}
