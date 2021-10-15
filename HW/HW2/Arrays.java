package arrays;
import com.sun.xml.internal.xsom.impl.scd.Iterators;
import org.checkerframework.checker.units.qual.C;

import java.util.*;
/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author Huyen Nguyen
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        /* *Replace this body with the solution. */
        int[] arr = new int[A.length + B.length];
        System.arraycopy(B,0,arr,0,B.length);
        System.arraycopy(A,0,arr,B.length, A.length);
        return arr;
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        /* *Replace this body with the solution. */
        if(len > A.length || len + start > A.length) return null;
        else {
            int[] arr = new int[A.length - len];
            if (start == 0)
                System.arraycopy(A, len, arr, 0, A.length - len);
            else{
                System.arraycopy(A, 0, arr, 0, start);
                System.arraycopy(A,start + len, arr, start, A.length - start - len);
            }
            return arr;
        }

    }

    /* C3. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {

        if(A == null)
            return null;
        if(A ==  new int[0]) return new int[0][0];
        int count = 0, n = 1;
        while(count  < A.length - 1) {
            if(A[count] > A[count + 1])
                n++;
            count++;
        }
        int[][]result = new int[n][];
        n = 0;
        while(A.length > 0) {
            if (n == result.length - 1) {
                result[n] = new int[A.length];
                System.arraycopy(A, 0, result[n], 0, A.length);
                A = Arrays.remove(A,0,A.length);
            } else {
                count = 0;
                while (A[count] < A[count + 1] && count + 1 < A.length)
                    count++;
                result[n] = new int[count + 1];
                System.arraycopy(A, 0, result[n], 0, result[n].length);
                A = Arrays.remove(A, 0, count + 1);
                n++;
            }
        }
        return result;
    }
}
