package dev.mohanverse.prime.enums;

import lombok.Getter;

public enum ExecutionType {
    PARALLEL("Parallel"),
    SEQUENTIAL("Sequential");

    @Getter
    private final String displayName;

    ExecutionType(String displayName) {
        this.displayName = displayName;
    }

}
