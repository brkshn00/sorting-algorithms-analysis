package core;

import algorithm.heapsort.HeapSort;
import algorithm.mergesort.MergeSort;
import algorithm.quicksort.QuickSort;

/**
 * Top-level API providing the exact void methods required by the assignment specification:
 * - void mergeSort(int[] arrayToSort, String numberOfPartitions)
 * - void heapSort(int[] arrayToSort)
 * - void quickSort(int[] arrayToSort, String pivotType)
 *
 * Rejects invalid parameter values with IllegalArgumentException.
 */
public class SortingAssignment {

    public void mergeSort(int[] arrayToSort, String numberOfPartitions) {
        if (numberOfPartitions == null) {
            throw new IllegalArgumentException("numberOfPartitions cannot be null.");
        }
        new MergeSort(numberOfPartitions).sort(arrayToSort);
    }

    public void heapSort(int[] arrayToSort) {
        new HeapSort().sort(arrayToSort);
    }

    public void quickSort(int[] arrayToSort, String pivotType) {
        if (pivotType == null) {
            throw new IllegalArgumentException("pivotType cannot be null.");
        }
        new QuickSort(pivotType).sort(arrayToSort);
    }

    // Static convenience methods allowing direct invocation without instantiation:
    public static void sortMerge(int[] arrayToSort, String numberOfPartitions) {
        new SortingAssignment().mergeSort(arrayToSort, numberOfPartitions);
    }

    public static void sortHeap(int[] arrayToSort) {
        new SortingAssignment().heapSort(arrayToSort);
    }

    public static void sortQuick(int[] arrayToSort, String pivotType) {
        new SortingAssignment().quickSort(arrayToSort, pivotType);
    }
}
