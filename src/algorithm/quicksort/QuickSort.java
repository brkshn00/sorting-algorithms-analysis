package algorithm.quicksort;

import algorithm.SortingAlgorithm;

/**
 * Quick Sort implementation utilizing the Hoare Partitioning Scheme
 * and interchangeable Pivot Strategies.
 *
 * Why Hoare Partitioning?
 * 1. Efficiency: Performs ~3x fewer swaps than Lomuto's scheme on average.
 * 2. Equal Elements: When all elements are identical, both scanning pointers stop
 *    at elements equal to the pivot, resulting in an even split near the middle.
 *    This preserves O(n log n) complexity on all-equal inputs, whereas Lomuto
 *    degrades to pathological O(n^2) with O(n) call stack depth.
 */
public class QuickSort implements SortingAlgorithm {

    private final PivotStrategy pivotStrategy;

    public QuickSort(PivotStrategy pivotStrategy) {
        if (pivotStrategy == null) {
            throw new IllegalArgumentException("PivotStrategy cannot be null.");
        }
        this.pivotStrategy = pivotStrategy;
    }

    public QuickSort(String pivotTypeStr) {
        if (pivotTypeStr == null) {
            throw new IllegalArgumentException("pivotType cannot be null.");
        }
        switch (pivotTypeStr.trim()) {
            case "FirstElement":
                this.pivotStrategy = new FirstElementPivot();
                break;
            case "RandomElement":
                this.pivotStrategy = new RandomElementPivot();
                break;
            case "MidOfFirstMidLastElement":
                this.pivotStrategy = new MedianOfThreePivot();
                break;
            default:
                throw new IllegalArgumentException("Invalid pivotType: '" + pivotTypeStr +
                        "'. Expected 'FirstElement', 'RandomElement', or 'MidOfFirstMidLastElement'.");
        }
    }

    @Override
    public String getName() {
        return "quickSort - " + pivotStrategy.getName();
    }

    public PivotStrategy getPivotStrategy() {
        return pivotStrategy;
    }

    @Override
    public void sort(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        quickSort(array, 0, array.length - 1);
    }

    private void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int partitionIndex = partition(arr, low, high);
            // Hoare partition divides array into [low .. partitionIndex]
            // and [partitionIndex + 1 .. high]
            quickSort(arr, low, partitionIndex);
            quickSort(arr, partitionIndex + 1, high);
        }
    }

    private int partition(int[] arr, int low, int high) {
        // Step 1: Select pivot using the configured strategy
        int pivotIndex = pivotStrategy.selectPivotIndex(arr, low, high);

        // Swap pivot with the first element (low) so we know pivot is at arr[low]
        if (pivotIndex != low) {
            swap(arr, low, pivotIndex);
        }

        int pivot = arr[low];
        int i = low - 1;
        int j = high + 1;

        while (true) {
            do {
                i++;
            } while (arr[i] < pivot);

            do {
                j--;
            } while (arr[j] > pivot);

            if (i >= j) {
                return j;
            }

            swap(arr, i, j);
        }
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
