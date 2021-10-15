package lists;
import java.util.*;
/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

/** List problem.
 *  @author Huyen Nguyen
 */
class Lists {

    /* B. */
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    static IntListList naturalRuns(IntList L) {
        /* *Replace this body with the solution. */
        if (L == null)
            return null;
        IntList P = L, T = L.tail;
        IntListList R = new IntListList();
        IntListList H = R;
        H.head = L;
        while(T != null) {
            if(P.head < T.head) {
                P = T;
                T = T.tail;


            }
            else {
                P.tail = null;
                L = T;
                P = T;
                T = T.tail;
                H.tail = new IntListList();
                H = H.tail;
                H.head = L;
            }
        }

        return R;
    }
}
