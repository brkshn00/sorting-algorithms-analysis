package algorithm.quicksort;

import java.util.Random;

/**
 * Pivot Strategy: RandomElement
 * Chooses a pivot uniformly at random from the range [low, high].
 *
 * Characteristics:
 * - Expected O(n log n) running time regardless of initial input order.
 * - Drastically reduces the probability of worst-case quadratic behavior.
 */
public class RandomElementPivot implements PivotStrategy {

    private final Random random;

    public RandomElementPivot() {
        this.random = new Random(42); // Fixed seed for reproducible benchmarks
    }

    public RandomElementPivot(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public int selectPivotIndex(int[] arr, int low, int high) {
        return low + random.nextInt(high - low + 1);
    }

    @Override
    public String getName() {
        return "RandomElement";
    }
}
