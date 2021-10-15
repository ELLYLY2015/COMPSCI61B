import com.sun.xml.internal.xsom.impl.scd.Iterators;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/** Minimal spanning tree utility.
 *  @author
 */
public class MST {

    /** Given an undirected, weighted, connected graph whose vertices are
     *  numbered 1 to V, and an array E of edges, returns an array of edges
     *  in E that form a minimal spanning tree of the input graph.
     *  Each edge in E is a three-element int array of the form (u, v, w),
     *  where 0 < u < v <= V are vertex numbers, and 0 <= w is the weight
     *  of the edge. The result is an array containing edges from E.
     *  Neither E nor the arrays in it may be modified.  There may be
     *  multiple edges between vertices.  The objects in the returned array
     *  are a subset of those in E (they do not include copies of the
     *  original edges, just the original edges themselves.) */
    public static int[][] mst(int V, int[][] E) {
        E = Arrays.copyOf(E, E.length);
        int numEdgesInResult = V - 1; // FIXME: how many edges should there be in our MST?
        int[][] result = new int[numEdgesInResult][];
        Arrays.sort(E, EDGE_WEIGHT_COMPARATOR);
        // FIXME: what other data structures do I need?
        // FIXME: do Kruskal's Algorithm
        UnionFind union = new UnionFind(V);
        HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>();
        if (E.length == 0) {
            return result;
        }
        int i = 0;
        while ( hash.size() != V)
        {
            if( !hash.containsKey( E[i][0] ) )
            {
                hash.put( E[i][0], hash.size() );
            }

            if( !hash.containsKey( E[i][1] ) )
            {
                hash.put( E[i][1], hash.size() );
            }
            i++;
        }

        int count_result = 0;
        int            j = 0;
        for ( i = 0; i < numEdgesInResult; i++ )
        {
            while ( union.samePartition( hash.get(E[j][0]), hash.get(E[j][1]))) {
                j++;
            }

            union.union( hash.get(E[j][0]), hash.get(E[j][1])  );

            result[count_result++] = E[j++];
        }

        return result;
    }

    /** An ordering of edges by weight. */
    private static final Comparator<int[]> EDGE_WEIGHT_COMPARATOR =
        new Comparator<int[]>() {
            @Override
            public int compare(int[] e0, int[] e1) {
                return e0[2] - e1[2];
            }
        };

}
