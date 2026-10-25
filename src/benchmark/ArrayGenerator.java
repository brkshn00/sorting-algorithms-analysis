package benchmark;

import java.util.Random;

/**
 * Generates synthetic benchmark arrays according to the assignment requirements:
 * - Sizes: 1,000, 10,000, 100,000
 * - Types: Equal, Random (fixed seed), Increasing, Decreasing
 */
public class ArrayGenerator {

    public enum InputType {
        EQUAL("EQUAL INTEGERS"),
        RANDOM("RANDOM INTEGERS"),
        INCREASING("INCREASING INTEGERS"),
        DECREASING("DECREASING INTEGERS");

        private final String displayName;

        InputType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private static final long FIXED_SEED = 42L;

    /**
     * Generates an array of the specified size and input pattern.
     *
     * @param size size of the array (e.g. 1000, 10000, 100000)
     * @param type the distribution pattern of the elements
     * @return freshly generated integer array
     */
    public static int[] generate(int size, InputType type) {
        int[] arr = new int[size];
        switch (type) {
            case EQUAL:
                // All elements are identical (e.g., 42)
                for (int i = 0; i < size; i++) {
                    arr[i] = 42;
                }
                break;

            case RANDOM:
                // Use a fixed seed to ensure repeatable, deterministic benchmarks
                Random random = new Random(FIXED_SEED);
                for (int i = 0; i < size; i++) {
                    arr[i] = random.nextInt(size * 10);
                }
                break;

            case INCREASING:
                // Strictly increasing sorted array: 0, 1, 2, ..., size - 1
                for (int i = 0; i < size; i++) {
                    arr[i] = i;
                }
                break;

            case DECREASING:
                // Strictly decreasing reverse-sorted array: size - 1, ..., 1, 0
                for (int i = 0; i < size; i++) {
                    arr[i] = size - 1 - i;
                }
                break;
        }
        return arr;
    }
}
