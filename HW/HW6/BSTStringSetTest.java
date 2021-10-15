import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class BSTStringSetTest  {
    // FIXME: Add your own tests for your BST StringSet
    BSTStringSet b = new BSTStringSet();
    @Test
    public void testPut_and_Contain() {
        // FIXME: Delete this function and add your own tests
        b.put("a");
        b.put("b");
        b.put("c");
        assertEquals(true, b.contains("a"));
        assertEquals(true, b.contains("b"));
        assertEquals(true, b.contains("b"));
        assertEquals(false, b.contains("s"));

        ArrayList<String> t = new ArrayList<>();
        t.add("a");
        t.add("b");
        t.add("c");
        assertEquals(t, b.asList());

    }
}
