package benchmark;

import algorithm.SortingAlgorithm;
import algorithm.heapsort.HeapSort;
import algorithm.mergesort.MergeSort;
import algorithm.quicksort.FirstElementPivot;
import algorithm.quicksort.MedianOfThreePivot;
import algorithm.quicksort.QuickSort;
import algorithm.quicksort.RandomElementPivot;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Executes running-time benchmark experiments to generate Appendix A table.
 *
 * Benchmark Rules:
 * 1. Tested sizes: 1,000, 10,000, 100,000.
 * 2. Input types: Equal, Random (fixed seed 42), Increasing, Decreasing.
 * 3. Array cloning: Each algorithm gets its own cloned copy; creation/clone time is excluded.
 * 4. Repetitions: 1 discarded warm-up run + 5 timed runs; reported value is the average in milliseconds.
 * 5. Correctness check: isSorted() is verified after every run outside the timing block.
 * 6. StackOverflow handling: StackOverflowError is caught, and "SO" is recorded in the table.
 * 7. Reference row: Arrays.sort(int[]) included as baseline.
 */
public class BenchmarkRunner {

    private static final int[] SIZES = new int[]{1_000, 10_000, 100_000};
    private static final ArrayGenerator.InputType[] TYPES = new ArrayGenerator.InputType[]{
            ArrayGenerator.InputType.EQUAL,
            ArrayGenerator.InputType.RANDOM,
            ArrayGenerator.InputType.INCREASING,
            ArrayGenerator.InputType.DECREASING
    };
    private static final int NUM_REPETITIONS = 5;

    public static void main(String[] args) {
        System.out.println("Starting Benchmark Experiments for Appendix A...\n");

        // Define algorithms to test
        Map<String, SortingAlgorithm> algorithms = new LinkedHashMap<>();
        algorithms.put("mergeSort - TwoParts", new MergeSort(MergeSort.PartitionType.TWO_PARTS));
        algorithms.put("mergeSort - ThreeParts", new MergeSort(MergeSort.PartitionType.THREE_PARTS));
        algorithms.put("heapSort", new HeapSort());
        algorithms.put("quickSort - FirstElement", new QuickSort(new FirstElementPivot()));
        algorithms.put("quickSort - RandomElement", new QuickSort(new RandomElementPivot()));
        algorithms.put("quickSort - MidOfFirstMidLastElement", new QuickSort(new MedianOfThreePivot()));

        // Also add Arrays.sort reference
        SortingAlgorithm arraysSortRef = new SortingAlgorithm() {
            @Override
            public void sort(int[] array) {
                Arrays.sort(array);
            }

            @Override
            public String getName() {
                return "Arrays.sort (reference, not graded)";
            }
        };
        algorithms.put("Arrays.sort (reference, not graded)", arraysSortRef);

        // Pre-generate original arrays (12 total inputs)
        // inputKey: "TYPE_SIZE"
        Map<String, int[]> datasets = new LinkedHashMap<>();
        for (ArrayGenerator.InputType type : TYPES) {
            for (int size : SIZES) {
                String key = type.name() + "_" + size;
                datasets.put(key, ArrayGenerator.generate(size, type));
            }
        }

        // Table result store: results[algoKey][datasetKey] -> String (ms or "SO")
        Map<String, Map<String, String>> results = new LinkedHashMap<>();
        for (String algoKey : algorithms.keySet()) {
            results.put(algoKey, new LinkedHashMap<>());
        }

        // Run experiments
        for (Map.Entry<String, SortingAlgorithm> algoEntry : algorithms.entrySet()) {
            String algoKey = algoEntry.getKey();
            SortingAlgorithm algo = algoEntry.getValue();
            System.out.println("Benchmarking: " + algoKey);

            for (ArrayGenerator.InputType type : TYPES) {
                for (int size : SIZES) {
                    String datasetKey = type.name() + "_" + size;
                    int[] original = datasets.get(datasetKey);

                    String measurementResult = benchmarkAlgorithm(algo, original);
                    results.get(algoKey).put(datasetKey, measurementResult);

                    System.out.printf("  %-20s | Size: %-7d | Result: %s ms%n",
                            type.getDisplayName(), size, measurementResult);
                }
            }
            System.out.println();
        }

        // Print final formatted table matching Appendix A
        printAppendixATable(results);
    }

    private static String benchmarkAlgorithm(SortingAlgorithm algo, int[] original) {
        try {
            // Step 1: Discarded warm-up run
            int[] warmUpCopy = original.clone();
            algo.sort(warmUpCopy);
            if (!ValidationUtil.isSorted(warmUpCopy)) {
                throw new IllegalStateException("Warm-up sort verification failed for " + algo.getName());
            }

            // Step 2: 5 timed runs
            long totalNanos = 0;
            for (int r = 0; r < NUM_REPETITIONS; r++) {
                int[] timedCopy = original.clone();

                long start = System.nanoTime();
                algo.sort(timedCopy);
                long elapsed = System.nanoTime() - start;

                totalNanos += elapsed;

                // Step 3: Verify correctness outside timed region
                if (!ValidationUtil.isSorted(timedCopy)) {
                    throw new IllegalStateException("Sort verification failed during benchmark for " + algo.getName());
                }
            }

            double avgMs = (totalNanos / (double) NUM_REPETITIONS) / 1_000_000.0;
            return String.format(java.util.Locale.US, "%.3f", avgMs);

        } catch (StackOverflowError soe) {
            return "SO";
        } catch (Throwable t) {
            return "ERR (" + t.getClass().getSimpleName() + ")";
        }
    }

    private static void printAppendixATable(Map<String, Map<String, String>> results) {
        System.out.println("\n" + "=".repeat(120));
        System.out.println("APPENDIX A - COMPARISON TABLE (running time in milliseconds)");
        System.out.println("=".repeat(120));

        // Header Line 1
        System.out.printf("%-35s | %-24s | %-24s | %-24s | %-24s |%n",
                "Algorithm",
                "EQUAL INTEGERS",
                "RANDOM INTEGERS",
                "INCREASING INTEGERS",
                "DECREASING INTEGERS");

        // Header Line 2 (Sizes)
        System.out.printf("%-35s | %-7s %-7s %-8s | %-7s %-7s %-8s | %-7s %-7s %-8s | %-7s %-7s %-8s |%n",
                "",
                "1,000", "10,000", "100,000",
                "1,000", "10,000", "100,000",
                "1,000", "10,000", "100,000",
                "1,000", "10,000", "100,000");
        System.out.println("-".repeat(145));

        for (Map.Entry<String, Map<String, String>> entry : results.entrySet()) {
            String algoName = entry.getKey();
            Map<String, String> row = entry.getValue();

            System.out.printf("%-35s | %-7s %-7s %-8s | %-7s %-7s %-8s | %-7s %-7s %-8s | %-7s %-7s %-8s |%n",
                    algoName,
                    row.get("EQUAL_1000"), row.get("EQUAL_10000"), row.get("EQUAL_100000"),
                    row.get("RANDOM_1000"), row.get("RANDOM_10000"), row.get("RANDOM_100000"),
                    row.get("INCREASING_1000"), row.get("INCREASING_10000"), row.get("INCREASING_100000"),
                    row.get("DECREASING_1000"), row.get("DECREASING_10000"), row.get("DECREASING_100000")
            );
        }
        System.out.println("=".repeat(145));
    }
}
