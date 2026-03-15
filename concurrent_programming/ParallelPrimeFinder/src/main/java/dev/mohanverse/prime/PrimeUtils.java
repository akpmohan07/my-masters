package dev.mohanverse.prime;

public class PrimeUtils {
    private PrimeUtils() {}; // Prevent instantiation

    public static boolean isPrime(long number) {
        if (number < 2) return false; // negatives, 0, 1
        if (number == 2) return  true; // 2 is prime
        if (number % 2 == 0) return false; // even numbers greater than 2 are not prime
        for (long i = 3; i <= Math.sqrt(number); i += 2)
            if (number % i == 0) return false;
        return true;
    }
}
