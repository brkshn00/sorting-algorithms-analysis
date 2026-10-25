package benchmark;

import algorithm.SortingAlgorithm;
import algorithm.heapsort.HeapSort;
import algorithm.mergesort.MergeSort;
import algorithm.quicksort.FirstElementPivot;
import algorithm.quicksort.MedianOfThreePivot;
import algorithm.quicksort.QuickSort;
import algorithm.quicksort.RandomElementPivot;
import core.SortingAssignment;

import java.util.Arrays;
import java.util.Random;

/**
 * Rigorous correctness and edge case test suite.
 * Verifies that all algorithms and parameter variants produce correctly sorted arrays
 * across all edge cases (lengths 0, 1, 2, not divisible by 2 or 3, duplicates, negatives),
 * and validates that invalid parameters throw IllegalArgumentException.
 */
public class CorrectnessTest {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("Running Correctness & Edge-Case Verification Tests");
        System.out.println("==================================================");

        SortingAlgorithm[] algorithms = new SortingAlgorithm[]{
                new MergeSort(MergeSort.PartitionType.TWO_PARTS),
                new MergeSort(MergeSort.PartitionType.THREE_PARTS),
                new HeapSort(),
                new QuickSort(new FirstElementPivot()),
                new QuickSort(new RandomElementPivot()),
                new QuickSort(new MedianOfThreePivot())
        };

        // Edge case arrays:
        int[][] edgeCases = new int[][]{
                {},                                 // length 0
                {42},                               // length 1
                {9, 3},                             // length 2 (descending)
                {3, 9},                             // length 2 (ascending)
                {7, 7},                             // length 2 (equal)
                {5, 1, 4},                          // length 3
                {10, -5, 2, 8},                     // length 4 (with negatives)
                {3, 3, 3, 3, 3},                    // length 5 (all equal)
                {7, 2, 9, 1, 5, 6, 8},              // length 7 (prime, not divisible by 2 or 3)
                {100, 90, 80, 70, 60, 50, 40, 30}   // reverse sorted
        };

        for (SortingAlgorithm algo : algorithms) {
            System.out.print("Testing " + algo.getName() + " on edge cases: ");
            for (int[] original : edgeCases) {
                int[] copy = original.clone();
                algo.sort(copy);

                if (!ValidationUtil.isSorted(copy)) {
                    System.err.println("\nFAILED on input: " + Arrays.toString(original));
                    System.err.println("Got: " + Arrays.toString(copy));
                    System.exit(1);
                }

                // Verify same length and elements
                int[] expected = original.clone();
                Arrays.sort(expected);
                if (!Arrays.equals(copy, expected)) {
                    System.err.println("\nFAILED (element mismatch) on: " + Arrays.toString(original));
                    System.exit(1);
                }
            }
            System.out.println("PASSED [OK]");
        }

        // Test random arrays of various non-divisible sizes
        Random rng = new Random(12345);
        int[] oddSizes = new int[]{11, 13, 17, 23, 37, 49, 101, 513};
        for (SortingAlgorithm algo : algorithms) {
            System.out.print("Testing " + algo.getName() + " on irregular sizes: ");
            for (int size : oddSizes) {
                int[] arr = new int[size];
                for (int i = 0; i < size; i++) {
                    arr[i] = rng.nextInt(1000) - 500;
                }
                int[] copy = arr.clone();
                algo.sort(copy);

                if (!ValidationUtil.isSorted(copy)) {
                    System.err.println("\nFAILED on size " + size);
                    System.exit(1);
                }
            }
            System.out.println("PASSED [OK]");
        }

        // Test IllegalArgumentException handling on SortingAssignment facade
        System.out.print("Testing IllegalArgumentException on invalid arguments: ");
        SortingAssignment assignment = new SortingAssignment();

        assertThrows(() -> assignment.mergeSort(new int[]{1, 2}, "FourParts"), "Invalid numberOfPartitions");
        assertThrows(() -> assignment.mergeSort(new int[]{1, 2}, null), "Null numberOfPartitions");
        assertThrows(() -> assignment.quickSort(new int[]{1, 2}, "LastElement"), "Invalid pivotType");
        assertThrows(() -> assignment.quickSort(new int[]{1, 2}, null), "Null pivotType");
        System.out.println("PASSED [OK]");

        System.out.println("\nALL CORRECTNESS TESTS PASSED SUCCESSFULLY!");
    }

    private static void assertThrows(Runnable runnable, String testCaseName) {
        try {
            runnable.run();
            System.err.println("\nFAILED: Expected IllegalArgumentException for " + testCaseName);
            System.exit(1);
        } catch (IllegalArgumentException expected) {
            // Success
        }
    }
}
