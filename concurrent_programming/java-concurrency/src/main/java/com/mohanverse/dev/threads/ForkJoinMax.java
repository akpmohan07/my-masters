package com.mohanverse.dev.threads;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinMax {

    int[] array = {3, 1, 9, 1, 5, 2};

    int findMaxSeq(int[] arr, int start, int end) {
        if (start == end) return arr[start];
        int mid = (start + end) / 2;
        int leftMax = findMaxSeq(arr, start, mid);
        int rightMax = findMaxSeq(arr, mid + 1, end);
        return Math.max(leftMax, rightMax);
    }

    public class MaxFinder extends RecursiveTask<Integer> {
        private final int[] arr;
        private final int start;
        private final int end;

        public MaxFinder(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (start == end) { return arr[start]; }
            int mid = (start + end) / 2;
            MaxFinder leftTask = new MaxFinder(arr, start, mid);
            MaxFinder rightTask = new MaxFinder(arr, mid + 1, end);
            leftTask.fork(); // Asynchronously execute the left task
            int rightResult = rightTask.compute(); // Compute right task
            int leftResult = leftTask.join(); // Wait for left task to complete and get result
            return Math.max(leftResult, rightResult);
        }
    }


    public static void main(String[] args) {
        ForkJoinMax forkJoinMax = new ForkJoinMax();
        int max = forkJoinMax.findMaxSeq(forkJoinMax.array, 0, forkJoinMax.array.length - 1);
        System.out.println("Maximum value found: " + max);

        ForkJoinPool pool = new ForkJoinPool();
        MaxFinder maxFinder = forkJoinMax.new MaxFinder(forkJoinMax.array, 0, forkJoinMax.array.length - 1);
        int parallelMax = pool.invoke(maxFinder);
        System.out.println("Maximum value found using ForkJoin: " + parallelMax);



    }
}
