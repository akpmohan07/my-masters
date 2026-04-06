package dev.mohanverse;

public abstract class MatrixMultiplier {

        public final double[][] multiply(double[][] A, double[][] B) {
            return doMultiply(A, B);
        }

        public abstract double[][] doMultiply(double[][] A, double[][] B);

}
