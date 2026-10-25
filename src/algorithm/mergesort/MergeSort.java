package algorithm.mergesort;

import algorithm.SortingAlgorithm;

/**
 * Merge Sort implementation supporting both standard Two-Way (TwoParts)
 * and Three-Way (ThreeParts) recursive splitting and merging.
 *
 * Design Decisions:
 * 1. Single Auxiliary Buffer: An auxiliary array of size n is allocated once
 *    at the top-level sort invocation and passed down the call chain.
 *    This avoids O(n log n) object allocations and prevents GC overhead from
 *    distorting benchmark measurements.
 * 2. ThreeParts Splitting: For length >= 3, the range is divided into 3 sub-arrays
 *    using mid1 = low + len / 3 and mid2 = low + 2 * len / 3.
 * 3. Base Cases for ThreeParts: Lengths <= 1 are already sorted.
 *    Length == 2 is resolved in O(1) by a single comparison/swap, preventing
 *    degenerate 0-sized partitions and infinite recursion.
 */
public class MergeSort implements SortingAlgorithm {

    public enum PartitionType {
        TWO_PARTS("TwoParts"),
        THREE_PARTS("ThreeParts");

        private final String value;

        PartitionType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static PartitionType fromString(String text) {
            for (PartitionType type : PartitionType.values()) {
                if (type.value.equalsIgnoreCase(text)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid numberOfPartitions: '" + text +
                    "'. Expected 'TwoParts' or 'ThreeParts'.");
        }
    }

    private final PartitionType partitionType;

    public MergeSort(PartitionType partitionType) {
        if (partitionType == null) {
            throw new IllegalArgumentException("PartitionType cannot be null.");
        }
        this.partitionType = partitionType;
    }

    public MergeSort(String partitionTypeStr) {
        this(PartitionType.fromString(partitionTypeStr));
    }

    @Override
    public String getName() {
        return "mergeSort - " + partitionType.getValue();
    }

    @Override
    public void sort(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        // Allocate auxiliary array once to minimize GC pressure during benchmarking
        int[] aux = new int[array.length];

        if (partitionType == PartitionType.TWO_PARTS) {
            sortTwoParts(array, aux, 0, array.length - 1);
        } else {
            sortThreeParts(array, aux, 0, array.length - 1);
        }
    }

    // =========================================================================
    // Two-Way Merge Sort (TwoParts)
    // =========================================================================

    private void sortTwoParts(int[] arr, int[] aux, int low, int high) {
        if (low >= high) {
            return;
        }
        int mid = low + (high - low) / 2;
        sortTwoParts(arr, aux, low, mid);
        sortTwoParts(arr, aux, mid + 1, high);
        mergeTwoParts(arr, aux, low, mid, high);
    }

    private void mergeTwoParts(int[] arr, int[] aux, int low, int mid, int high) {
        // Copy elements in the range to aux
        System.arraycopy(arr, low, aux, low, high - low + 1);

        int i = low;       // Pointer for left sub-array [low, mid]
        int j = mid + 1;   // Pointer for right sub-array [mid + 1, high]
        int k = low;       // Pointer for merged array in arr

        while (i <= mid && j <= high) {
            if (aux[i] <= aux[j]) { // Stable: <= preserves relative order
                arr[k++] = aux[i++];
            } else {
                arr[k++] = aux[j++];
            }
        }

        // Copy remaining elements of left sub-array if any
        while (i <= mid) {
            arr[k++] = aux[i++];
        }

        // Note: remaining elements of right sub-array are already in place
    }

    // =========================================================================
    // Three-Way Merge Sort (ThreeParts)
    // =========================================================================

    private void sortThreeParts(int[] arr, int[] aux, int low, int high) {
        int len = high - low + 1;
        if (len <= 1) {
            return;
        }
        // Specific requirement: handling sub-arrays of length 2
        if (len == 2) {
            if (arr[low] > arr[high]) {
                int temp = arr[low];
                arr[low] = arr[high];
                arr[high] = temp;
            }
            return;
        }

        // Split into 3 parts:
        // Part 1: [low, mid1 - 1]
        // Part 2: [mid1, mid2 - 1]
        // Part 3: [mid2, high]
        int mid1 = low + len / 3;
        int mid2 = low + 2 * len / 3;

        sortThreeParts(arr, aux, low, mid1 - 1);
        sortThreeParts(arr, aux, mid1, mid2 - 1);
        sortThreeParts(arr, aux, mid2, high);

        mergeThreeParts(arr, aux, low, mid1, mid2, high);
    }

    private void mergeThreeParts(int[] arr, int[] aux, int low, int mid1, int mid2, int high) {
        // Copy relevant segment to aux
        System.arraycopy(arr, low, aux, low, high - low + 1);

        int i = low;       // Pointer for Part 1: [low, mid1 - 1]
        int j = mid1;      // Pointer for Part 2: [mid1, mid2 - 1]
        int k = mid2;      // Pointer for Part 3: [mid2, high]
        int l = low;       // Target pointer in arr

        // Pick smallest element among the 3 sub-arrays
        while (i < mid1 && j < mid2 && k <= high) {
            if (aux[i] <= aux[j]) {
                if (aux[i] <= aux[k]) {
                    arr[l++] = aux[i++];
                } else {
                    arr[l++] = aux[k++];
                }
            } else {
                if (aux[j] <= aux[k]) {
                    arr[l++] = aux[j++];
                } else {
                    arr[l++] = aux[k++];
                }
            }
        }

        // Now at most 2 sub-arrays have elements remaining:
        // Case A: Part 1 and Part 2 remain
        while (i < mid1 && j < mid2) {
            if (aux[i] <= aux[j]) {
                arr[l++] = aux[i++];
            } else {
                arr[l++] = aux[j++];
            }
        }

        // Case B: Part 2 and Part 3 remain
        while (j < mid2 && k <= high) {
            if (aux[j] <= aux[k]) {
                arr[l++] = aux[j++];
            } else {
                arr[l++] = aux[k++];
            }
        }

        // Case C: Part 1 and Part 3 remain
        while (i < mid1 && k <= high) {
            if (aux[i] <= aux[k]) {
                arr[l++] = aux[i++];
            } else {
                arr[l++] = aux[k++];
            }
        }

        // Finally, copy any leftover from the single remaining sub-array
        while (i < mid1) {
            arr[l++] = aux[i++];
        }
        while (j < mid2) {
            arr[l++] = aux[j++];
        }
        while (k <= high) {
            arr[l++] = aux[k++];
        }
    }
}
