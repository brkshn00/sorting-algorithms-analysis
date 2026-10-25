package algorithm.quicksort;

/**
 * Strategy interface for selecting the pivot in Quick Sort.
 * Follows the Strategy Design Pattern to make pivot selection algorithms
 * interchangeable without modifying the main Quick Sort algorithm.
 */
public interface PivotStrategy {
    /**
     * Determines the pivot index within the range [low, high].
     *
     * @param arr the array being sorted
     * @param low the starting index (inclusive)
     * @param high the ending index (inclusive)
     * @return the index of the chosen pivot element
     */
    int selectPivotIndex(int[] arr, int low, int high);

    /**
     * Returns the name of the pivot strategy matching assignment specifications.
     */
    String getName();
}
