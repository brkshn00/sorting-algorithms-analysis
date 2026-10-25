package algorithm.heapsort;

import algorithm.SortingAlgorithm;

/**
 * In-place Heap Sort implementation using a Binary Max-Heap.
 *
 * Algorithmic Details & Defense Points:
 * 1. In-Place: Uses the array directly without allocating extra data structures (O(1) auxiliary space).
 * 2. Array Representation:
 *    - Left Child:  2 * i + 1
 *    - Right Child: 2 * i + 2
 *    - Parent:      (i - 1) / 2
 * 3. Why buildMaxHeap is Theta(n):
 *    Nodes at height h cost O(h) to sift down. The number of nodes at height h is at most n / 2^(h+1).
 *    Summing (n / 2^(h+1)) * h over all h yields n * sum(h / 2^(h+1)) = n * 1 = Theta(n).
 * 4. Overall Time Complexity:
 *    Phase 1 (build heap): Theta(n)
 *    Phase 2 (n-1 extractions of max): Theta(n log n)
 *    Total: Theta(n log n) across best, average, and worst cases.
 */
public class HeapSort implements SortingAlgorithm {

    @Override
    public String getName() {
        return "heapSort";
    }

    @Override
    public void sort(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        int n = array.length;

        // Step 1: Build binary max-heap in Theta(n) time
        // Start from the last non-leaf node: (n / 2) - 1 down to 0
        for (int i = (n / 2) - 1; i >= 0; i--) {
            heapify(array, n, i);
        }

        // Step 2: Extract elements from max-heap one by one in Theta(n log n) time
        for (int i = n - 1; i > 0; i--) {
            // Move current root (maximum element) to the end of the unsorted segment
            swap(array, 0, i);

            // Restore max-heap property on the reduced heap of size i
            heapify(array, i, 0);
        }
    }

    /**
     * Sifts down the element at index i in a max-heap of size heapSize.
     *
     * @param arr      the array representing the binary heap
     * @param heapSize the current number of elements active in the heap
     * @param i        the root index of the subtree to heapify
     */
    private void heapify(int[] arr, int heapSize, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < heapSize && arr[left] > arr[largest]) {
            largest = left;
        }

        if (right < heapSize && arr[right] > arr[largest]) {
            largest = right;
        }

        // If largest is not root, swap and continue sifting down iteratively or recursively
        if (largest != i) {
            swap(arr, i, largest);
            heapify(arr, heapSize, largest);
        }
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
