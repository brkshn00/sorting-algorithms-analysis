# AIE303 - Design and Analysis of Algorithms
## Assignment 1: Empirical and Theoretical Comparison of Merge Sort, Heap Sort, and Quick Sort

An object-oriented Java implementation and empirical performance analysis comparing **Merge Sort** (Two-Way and Three-Way), **Heap Sort** (In-Place Binary Max-Heap), and **Quick Sort** (FirstElement, RandomElement, and MidOfFirstMidLastElement using Hoare Partitioning).

---

## 📌 Project Architecture & Design Principles

The codebase adheres strictly to object-oriented programming (OOP) principles:
- **Polymorphism via `SortingAlgorithm` Interface:** Allows benchmarks and test harnesses to treat all algorithms uniformly.
- **Strategy Pattern for Quick Sort Pivots (`PivotStrategy`):** Decouples pivot selection strategies (`FirstElementPivot`, `RandomElementPivot`, `MedianOfThreePivot`) from the core partitioning engine, enabling extensibility without code duplication.
- **Single Auxiliary Buffer Optimization in Merge Sort:** Allocates the auxiliary buffer of size $n$ once at top-level invocation, preventing $O(n \log n)$ array allocations and eliminating garbage collection (GC) jitter during benchmarking.
- **Hoare Partitioning Scheme:** Selected over Lomuto because it performs ~3x fewer swaps on average and handles all-equal elements (`Equal Integers`) symmetrically, preserving $\Theta(n \log n)$ performance without degenerating to $O(n^2)$.
- **In-Place Heap Sort:** Builds a binary max-heap bottom-up in $\Theta(n)$ time and performs $n-1$ extractions in $\Theta(n \log n)$ time with $O(1)$ auxiliary space.

```
src/
├── algorithm/
│   ├── SortingAlgorithm.java           # Common sorting interface
│   ├── mergesort/
│   │   └── MergeSort.java              # 2-way and 3-way Merge Sort
│   ├── quicksort/
│   │   ├── QuickSort.java              # Hoare partition QuickSort
│   │   ├── PivotStrategy.java          # Pivot strategy interface
│   │   ├── FirstElementPivot.java      # First element pivot strategy
│   │   ├── RandomElementPivot.java     # Random element pivot strategy
│   │   └── MedianOfThreePivot.java     # MidOfFirstMidLastElement strategy
│   └── heapsort/
│       └── HeapSort.java               # In-place Max-Heap Sort
├── core/
│   └── SortingAssignment.java          # Assignment facade with required method signatures
└── benchmark/
    ├── ArrayGenerator.java             # Synthetic benchmark array generator (Equal, Random, Inc, Dec)
    ├── ValidationUtil.java             # Correctness verification (isSorted)
    ├── CorrectnessTest.java            # Comprehensive test suite covering edge cases
    └── BenchmarkRunner.java            # Appendix A benchmark runner (1 warm-up + 5 timed runs)
```

---

## 📊 Experimental Results (Appendix A Table)

*Measured average execution times in milliseconds (ms) over 5 timed runs after 1 discarded warm-up run (`System.nanoTime()`):*

| Algorithm | EQUAL (1K) | EQUAL (10K) | EQUAL (100K) | RANDOM (1K) | RANDOM (10K) | RANDOM (100K) | INCREASING (1K) | INCREASING (10K) | INCREASING (100K) | DECREASING (1K) | DECREASING (10K) | DECREASING (100K) |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **mergeSort - TwoParts** | 0.021 | 0.482 | 0.986 | 0.035 | 0.483 | 6.955 | 0.008 | 0.089 | 1.042 | 0.012 | 0.147 | 1.828 |
| **mergeSort - ThreeParts** | 0.058 | 0.206 | 0.808 | 0.089 | 0.511 | 6.991 | 0.007 | 0.087 | 0.889 | 0.009 | 0.109 | 1.144 |
| **heapSort** | 0.035 | 0.188 | 0.428 | 0.071 | 0.590 | 6.870 | 0.018 | 0.306 | 3.911 | 0.015 | 0.306 | 4.161 |
| **quickSort - FirstElement** | 0.044 | 0.345 | 0.860 | 0.177 | 0.472 | 4.960 | **0.237** | **21.714** | **SO** | **0.180** | **16.397** | **SO** |
| **quickSort - RandomElement** | 0.117 | 0.578 | 1.575 | 0.044 | 0.513 | 7.576 | 0.043 | 0.235 | 2.301 | 0.036 | 0.250 | 2.377 |
| **quickSort - MidOfFirstMidLastElement** | 0.090 | 0.232 | 1.418 | 0.031 | 0.411 | 5.434 | 0.009 | 0.164 | 1.129 | 0.009 | 0.101 | 1.120 |
| *Arrays.sort (reference)* | 0.005 | 0.049 | 0.205 | 0.136 | 0.349 | 5.506 | 0.003 | 0.018 | 0.167 | 0.003 | 0.029 | 0.241 |

*Note: **SO** indicates `StackOverflowError` caused by $O(n)$ recursion depth on sorted inputs.*

---

## 🔬 Complexity & Empirical Analysis

1. **$\Theta(n \log n)$ Scaling:**
   When input size increases $10\times$ from $10,000$ to $100,000$, $\Theta(n \log n)$ predicts:
   $$\frac{100,000 \log_2(100,000)}{10,000 \log_2(10,000)} = 10 \times \frac{16.61}{13.29} \approx 12.5\times \text{ increase}$$
   - `mergeSort - TwoParts` on random integers scaled from $0.483\text{ ms} \to 6.955\text{ ms}$ ($14.4\times$).
   - `heapSort` on random integers scaled from $0.590\text{ ms} \to 6.870\text{ ms}$ ($11.6\times$).
   Both match asymptotic theory with minor cache hierarchy effects.

2. **$\Theta(n^2)$ Degeneration & Stack Exhaustion:**
   - `quickSort - FirstElement` on sorted inputs exhibited severe quadratic degradation: increasing $N$ by $10\times$ ($1,000 \to 10,000$) caused runtime to spike by $\approx 91.6\times$ ($0.237\text{ ms} \to 21.714\text{ ms}$). At $N = 100,000$, the recursion depth reached $100,000$, triggering `StackOverflowError`.
   - In contrast, `MidOfFirstMidLastElement` chose the true median at each partition, maintaining $O(\log n)$ recursion depth and completing in $1.129\text{ ms}$.

---

## 🛠 Compilation & Execution

Compile all source files:
```bash
javac -d out $(find src -name "*.java")
```

Run correctness test suite:
```bash
java -cp out benchmark.CorrectnessTest
```

Run benchmark experiments (Appendix A table generator):
```bash
java -cp out benchmark.BenchmarkRunner
```
