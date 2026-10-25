import algorithm.heapsort.HeapSort;
import algorithm.mergesort.MergeSort;
import algorithm.quicksort.QuickSort;
import benchmark.BenchmarkRunner;
import benchmark.CorrectnessTest;

/**
 * Assignment Test Class.
 *
 * Implements the required top-level methods:
 * - void mergeSort(int[] arrayToSort, String numberOfPartitions)
 * - void heapSort(int[] arrayToSort)
 * - void quickSort(int[] arrayToSort, String pivotType)
 *
 * Rejects invalid parameter values with IllegalArgumentException.
 */
public class Test {

    public static void mergeSort(int[] arrayToSort, String numberOfPartitions) {
        if (numberOfPartitions == null) {
            throw new IllegalArgumentException("numberOfPartitions cannot be null.");
        }
        new MergeSort(numberOfPartitions).sort(arrayToSort);
    }

    public static void heapSort(int[] arrayToSort) {
        new HeapSort().sort(arrayToSort);
    }

    public static void quickSort(int[] arrayToSort, String pivotType) {
        if (pivotType == null) {
            throw new IllegalArgumentException("pivotType cannot be null.");
        }
        new QuickSort(pivotType).sort(arrayToSort);
    }

    public static void main(String[] args) {
        System.out.println("======================================================");
        System.out.println("AIE303 - Design and Analysis of Algorithms - Assignment 1");
        System.out.println("Comparison of Merge Sort, Heap Sort and Quick Sort");
        System.out.println("======================================================\n");

        // 1. Run correctness tests
        System.out.println("[1/2] Running Correctness & Edge-Case Suite...");
        CorrectnessTest.main(new String[0]);

        System.out.println("\n[2/2] Running Benchmark Experiments (Appendix A Table)...");
        BenchmarkRunner.main(new String[0]);
    }
}
