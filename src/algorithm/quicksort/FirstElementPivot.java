package algorithm.quicksort;

/**
 * Pivot Strategy: FirstElement
 * Always selects the first element (low) of the (sub)array.
 *
 * Characteristics:
 * - Simple O(1) selection with zero overhead.
 * - Pathological worst-case O(n^2) time and O(n) call stack depth when the input
 *   is already sorted (increasing) or reverse sorted (decreasing), leading to StackOverflowError.
 */
public class FirstElementPivot implements PivotStrategy {

    @Override
    public int selectPivotIndex(int[] arr, int low, int high) {
        return low;
    }

    @Override
    public String getName() {
        return "FirstElement";
    }
}
