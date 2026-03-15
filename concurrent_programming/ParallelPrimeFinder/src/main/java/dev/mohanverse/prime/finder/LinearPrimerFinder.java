package dev.mohanverse.prime.finder;

import dev.mohanverse.prime.metrics.PrimeFinderMetrics;

import java.util.List;

public class LinearPrimerFinder extends  PrimeFinderAbstract{
    PrimeFinderMetrics primeFinderMetrics = new PrimeFinderMetrics();



    @Override
    public List<Integer> findPrimeForRange(int start, int end) {
        for (int i = start; i <= end; i++) {
            if (isPrime(i)) {
                System.out.println(i);
            }
        }
        return null;
    }

    @Override
    public boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        for (int i = 2; i < number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
