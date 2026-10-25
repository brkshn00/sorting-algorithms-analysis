package algorithm;

/**
 * Common interface for all sorting algorithm implementations.
 * Enables polymorphism so that benchmarking, testing, and execution
 * can treat any sorting algorithm uniformly.
 */
public interface SortingAlgorithm {
    /**
     * Sorts the given array in-place in ascending order.
     *
     * @param array the array of integers to be sorted
     */
    void sort(int[] array);

    /**
     * Returns a human-readable name of the specific algorithm variant.
     *
     * @return name string for reporting and table generation
     */
    String getName();
}
