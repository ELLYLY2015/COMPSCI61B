import java.util.HashMap;
import java.util.Set;

/** Disjoint sets of contiguous integers that allows (a) finding whether
 *  two integers are in the same set and (b) unioning two sets together.  
 *  At any given time, for a structure partitioning the integers 1 to N, 
 *  into sets, each set is represented by a unique member of that
 *  set, called its representative.
 *  @author
 */
public class UnionFind {

    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */
    public UnionFind(int N) {
        // FIXME
        parent = new int[N];
        for (int i = 0; i < N; i++) {
            parent[i] = i;
        }
    }

    /** Return the representative of the set currently containing V.
     *  Assumes V is contained in one of the sets.  */
    public int find(int v) {

        int root = parent[v];
        while(root != parent[root]) {
            root = parent[root];// FIXME
        }
        return root;
    }

    /** Return true iff U and V are in the same set. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single set, returning its representative. */
    public int union(int u, int v) {

        if(samePartition(u,v)) {
            return find(u);
        }  // FIXME
        int root_v = find(v);
        parent[find(u)] = find(v);
        return root_v;
    }

    int[] parent;// FIXME
}
