package dev.mohanverse.prime.finder;

import java.util.List;

public abstract class PrimeFinderAbstract {

    public abstract List<Integer> findPrimeForRange(int start, int end);

    public abstract boolean isPrime(int number);
}
