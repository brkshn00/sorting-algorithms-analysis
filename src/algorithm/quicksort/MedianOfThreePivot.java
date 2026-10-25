package algorithm.quicksort;

/**
 * Pivot Strategy: MidOfFirstMidLastElement
 * The pivot is the element whose value is the median of {first, middle, last}.
 *
 * Characteristics:
 * - Extremely effective against already sorted and reverse sorted inputs.
 * - Prevents O(n^2) behavior on canonical sorted/reverse-sorted benchmarks.
 * - Low constant factor overhead (at most 3 comparisons).
 */
public class MedianOfThreePivot implements PivotStrategy {

    @Override
    public int selectPivotIndex(int[] arr, int low, int high) {
        int mid = low + (high - low) / 2;

        int a = arr[low];
        int b = arr[mid];
        int c = arr[high];

        // Find which index holds the median value among a, b, c
        if ((a <= b && b <= c) || (c <= b && b <= a)) {
            return mid;
        } else if ((b <= a && a <= c) || (c <= a && a <= b)) {
            return low;
        } else {
            return high;
        }
    }

    @Override
    public String getName() {
        return "MidOfFirstMidLastElement";
    }
}
