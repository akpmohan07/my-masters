package dev.mohanverse.prime.enums;

import lombok.Getter;

public enum Algorithm {
    LINEAR_TRIAL_DIVISION("Linear Trial Division"),
    SIEVE_OF_ERATOSTHENES("Sieve of Eratosthenes");

    @Getter
    private final String displayName;

    Algorithm(String displayName) {
        this.displayName = displayName;
    }
}
