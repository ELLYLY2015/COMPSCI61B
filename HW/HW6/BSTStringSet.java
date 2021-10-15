import java.awt.color.ICC_ProfileRGB;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author
 */
public class BSTStringSet implements SortedStringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        // FIXME: PART A
        _root = help_Put(s, _root);
    }

    public Node help_Put(String c, Node p) {
        if(p == null) {
            p = new Node(c);
            p.left = p.right = null;
        } else {
                if (p.s.compareTo(c) < 0) {
                    p.left = help_Put(c, p.left);
                } else {
                    p.right = help_Put(c, p.right);
                }
            }
        return p;
    }

    @Override
    public boolean contains(String s) {
        return help_Contains(s, _root); // FIXME: PART A
    }

    public boolean help_Contains(String s, Node p) {
        if(p != null) {
            if(p.s.equals(s)) {
                return true;
            } else {
                if (p.s.compareTo(s) < 0) {
                    return help_Contains(s, p.left);
                } else {
                    return help_Contains(s, p.right);
                }
            }
        }
        return false;
    }
    @Override
    public List<String> asList() {
        ArrayList<String> temp = new ArrayList<>();
        Iterator<String> ite = iterator();
        while(ite.hasNext()) {
            temp.add(ite.next());
        }
        int i = temp.size() - 1;
        ArrayList<String> result = new ArrayList<>();
        while(i > -1) {
            result.add(temp.get(i));
            i--;
        }
        return result; // FIXME: PART A
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    // FIXME: UNCOMMENT THE NEXT LINE FOR PART B
    @Override
    public Iterator<String> iterator(String low, String high) {
        Iterator ite = new BSTIterator(_root);
        return new BoundedBSTIterator(low, high);  // FIXME: PART B
    }
    private class BoundedBSTIterator implements Iterator<String> {
        private Stack<Node> _toDo;
        private Node node;
        private String low;
        private String high;
        public BoundedBSTIterator(String L, String H) {
            _toDo = new Stack<>();
            low = L;
            high = H;
            addTree(_root);
        }

        @Override
        public boolean hasNext() {
            return !(_toDo.empty() || (node != null && node.s.compareTo(low) <= 0));
        }

        private void addTree(Node node) {
            while (node != null && node.s.compareTo(low) >= 0 && node.s.compareTo(high) <= 0) {
                _toDo.push(node);
                node = node.left;
            }
            if (node != null) {
                addTree(node.right);
            }
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

    }

    /** Root node of the tree. */
    private Node _root;
}
