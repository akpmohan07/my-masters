package dev.mohanverse;

import dev.mohanverse.core.MatrixBenchmark;
import dev.mohanverse.utils.MatrixUtils;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        double[][] a = MatrixUtils.generateMatrix(4,4);
        MatrixUtils.printMatrix(a);

        MatrixBenchmark benchmark = new MatrixBenchmark();
        benchmark.run();
    }
}