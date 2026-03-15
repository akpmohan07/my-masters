package dev.mohanverse;

import dev.mohanverse.prime.config.PrimeFinderConfig;
import dev.mohanverse.prime.executor.PrimerFinderExecutor;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        PrimeFinderConfig.load();
        PrimerFinderExecutor executor = new PrimerFinderExecutor(PrimeFinderConfig.getTestValues());
        executor.execute();
    }
}