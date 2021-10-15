import java.util.Arrays;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
            k = Math.min(k, array.length);
            for (int i = 1; i < k; ++i) {
                int key = array[i];
                int j = i - 1;

                while (j >= 0 && array[j] > key) {
                    array[j + 1] = array[j];
                    j = j - 1;
                }
                array[j + 1] = key;
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
            k = Math.min(k, array.length);
            for (int i = 0; i < k; i += 1) {
                int min = i;
                for (int j = i + 1; j < k; j += 1) {
                    if (array[min] > array[j]) {
                        min = j;
                    }
                }
                swap(array, min, i);
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            k = Math.min(k, array.length);
            mergesort(array, 0, k, new int[k]);
        }

        // may want to add additional methods
        private void mergesort(int[] a, int left, int right, int[] b) {
            if (left + 1 < right) {
                int mid = (left + right) / 2;
                mergesort(a, left, mid, b);
                mergesort(a, mid, right, b);
                merge(a, left, mid, right, b);
                System.arraycopy(b, left, a, left, right - left);
            }
        }
        private void merge(int[] array, int left, int mid, int right,
                           int[] js) {

            for(int l = left, r = mid; l < mid || r < right;){
                if((r == right) || (l < mid && array[l] < array[r]))
                {
                    js[l+r-mid] = array[l];
                    l++;
                } else if((l == mid) ||  (r < right && array[l] >= array[r])){
                    js[l+r-mid] = array[r];
                    r++;
                }
            }
        }
        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
            k = Math.min(k, array.length);
            int[] b = new int[k]; // The output will have sorted input array
            int max = array[0];
            int min = array[0];

            int i;
            for(i = 1; i < k; i++)
            {
                if(array[i] > max)
                    max = array[i];
                else if(array[i] < min)
                    min = array[i];
            }

            int m = max - min + 1;

            int[] count_array = new int[m];
            for(i=0; i<k; i++)
                count_array[i]=0;

            for(i = 0; i < k; i++)
                count_array[array[i] - min]++;

            for(i = 1; i < k; i++)
                count_array[i] += count_array[i - 1];


            for(i = 0; i < k; i++)
            {
                b[count_array[array[i] - min] - 1] = array[i];
                count_array[array[i] - min]--;
            }

            for(i = 0; i < k; i++)
                array[i] = array[i];
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            k = Math.min(k, array.length);
            int h, m, i, j;
            boolean ok;
            for (h = k/2; h >= 1; h--) {
                i = h; ok = false;
                while ((i <= k/2) && !ok) {
                    if (i == k/2 && 2*i == k) {
                        j = 2*i;
                    } else {
                        if ((array[2 *i]) < array[2*i + 1]) {
                            j = 2*i;
                        } else {
                            j = 2*i + 1;
                        }
                    }
                    if (array[i] > array[j]) {
                        swap(array, i, j);
                        i = j;
                    } else {
                        ok = true;
                    }
                }
            }
            for (m = k; m >= 2; m--) {
                swap(array, 1, m);
                i = 1; ok = false;
                while((i <= (m -1)/2) && !ok) {
                    if (i == (m - 1)/ 2 && 2*i == k -1) {
                        j = 2*i;
                    } else {
                        if (array[2*i] < array[2*i + 1]) {
                            j = 2*i;
                        } else {
                            j = 2*i + 1;
                        }
                        if (array[i] > array[j]) {
                            swap(array, i, j);
                        } else {
                            ok = true;
                        }
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
            k = Math.min(k,array.length);
            quickSort(array,0,k);

        }
        private void quickSort(int[] array, int left, int right) {

            if (left < right) {
                int m = partition (array, left, right);
                quickSort(array, left, m - 1);
                quickSort(array, m + 1, right);
            }
        }
        private int partition(int[] a, int left, int right) {
            int pivot = a[left];
            int i = (left-1); // index of smaller element
            for (int j=left; j<right; j++)
            {
                if (a[j] < pivot)
                {
                    i++;
                    swap(a,i,j);
                }
            }
            swap(a, i+1, right);
            return i+1;
        }
        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
            k = Math.min(k,a.length);
            int i, m = a[0], exp = 1;
            int[] B = new int[k];
            for (i = 1; i < k; i++)
            {
                if (a[i] > m) {
                    m = a[i];
                }
            }
            while (m / exp > 0) {
                int[] bucket = new int[10];
                for (i = 0; i < k; i++)
                {
                    bucket[(a[i] / exp) % 10]++;
                }
                for (i = 1; i < 10; i++) {
                    bucket[i] += bucket[i - 1];
                }
                for (i = k - 1; i >= 0; i--) {
                    B[--bucket[(a[i] / exp) % 10]] = a[i];
                }
                for (i = 0; i < k; i++) {
                    a[i] = B[i];
                }
                exp *= 10;
            }

        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
