package benchmark;

/**
 * Utility class to verify correctness of sorted arrays.
 * This check must be performed outside the timed region of the benchmark.
 */
public final class ValidationUtil {

    private ValidationUtil() {
        // Prevent instantiation of utility class
    }

    /**
     * Checks whether the given array is sorted in ascending (non-decreasing) order.
     *
     * @param array the array to check
     * @return true if sorted or empty/single element, false otherwise
     */
    public static boolean isSorted(int[] array) {
        if (array == null || array.length <= 1) {
            return true;
        }
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }
}
