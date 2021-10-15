import java.awt.dnd.DnDConstants;

/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Vivant Sakore (1/29/2020)
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        // FIXME: Implement this method and return correct value
        DNode p = _front;
        int count = 0;
        while(p != null){
            count += 1;
            p = p._next;
        }
        return count;
    }

    /**
     * @param i index of element to return,
     *          where i = 0 returns the first element,
     *          i = 1 returns the second element,
     *          i = -1 returns the last element,
     *          i = -2 returns the second to last element, and so on.
     *          You can assume i will always be a valid index, i.e 0 <= i < size for positive indices
     *          and -size <= i <= -1 for negative indices.
     * @return The integer value at index i
     */
    public int get(int i) {
        // FIXME: Implement this method and return correct value
        DNode p = null;
        if(i > size() || i < (-size() - 1)) return -1;
        else{
            if(i < 0){
                p = _back;
                while(p != null && p._prev != null && i < -1){
                    p = p._prev;
                    i += 1;
                }
            }
            else{
                p = _front;
                while(p != null && p._next != null && i > 0){
                    p = p._next;
                    i -= 1;
                }
            }
        }
        return p._val;
    }
    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        // FIXME: Implement this method
        DNode p = new DNode(d);
        if(_front == null)
            _front = _back = p;
        else {
            p._next = _front;
            _front._prev = p;
            _front = p ;
        }
    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        // FIXME: Implement this method
        DNode p = new DNode(d);
        if(_front == null)
            _front = _back = p;
        else{
            p._prev = _back;
            _back._next = p;
            _back = p;
        }
    }

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position,
     *              index = -1 inserts at the back,
     *              index = -2 inserts at the second to last position, and so on.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size for positive indices (including insertions at front and back)
     *              and -(size+1) <= index <= -1 for negative indices (including insertions at front and back).
     */
    public void insertAtIndex(int d, int index) {
        // FIXME: Implement this method
        DNode p = new DNode(d);
        if(index == 0 || index == -(size() + 1))
            insertFront(d);
        else if (index == size() || index == -1)
            insertBack(d);
        else {
            DNode temp = null;
            if(index < 0){
                temp = _back;
                while(temp != null && temp._prev != null && index < -1){
                    temp = temp._prev;
                    index += 1;
                }
                p._next = temp._next;
                p._prev = temp;
                temp._next._prev = p;
                temp._next = p;

            }
            else{
                temp = _front;
                while(temp != null && temp._next != null && index > 0){
                    temp = temp._next;
                    index -= 1;
                }
                p._next = temp;
                p._prev = temp._prev;
                temp._prev._next = p;
                temp._prev = p;

            }

        }
    }

    /**
     * Removes the first item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {
        // FIXME: Implement this method and return correct value
        DNode p = _front;
        if(_front == null) return -1;
        else{
            if(_front == _back)
                _front = _back = null;
            else {
                _front = _front._next;
                _front._prev = null;
            }
        }
        return p._val;
    }

    /**
     * Removes the last item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        // FIXME: Implement this method and return correct value
        DNode p = _back;
        if (_front == null) return -1;
        else {
            if (_front == _back)
                _front = _back = null;
            else {
                _back = _back._prev;
                _back._next = null;
            }
        }
        return p._val;
    }

    /**
     * @param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element,
     *          index = -1 will delete the last element,
     *          index = -2 will delete the second to last element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size for positive indices (including deletions at front and back)
     *              and -size <= index <= -1 for negative indices (including deletions at front and back).
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        DNode p = null;
        // FIXME: Implement this method and return correct value
        if(index == 0 || index == -size()) return deleteFront();
        else  if (index == size() - 1 || index == -1) return deleteBack();
        else {
            if (index > size() + 1 || index < (-size() - 1)) return -1;
            else {
                if (index < 0) {
                    p = _back;
                    while (p != null && p._prev != null && index < -1) {
                        p = p._prev;
                        index += 1;
                    }


                } else {
                    p = _front;
                    while (p != null && p._next != null && index > 0) {
                        p = p._next;
                        index -= 1;
                    }

                }

                p._prev._next = p._next;
                p._next._prev = p._prev;
                p._prev = p._next = null;

            }
        }
        return p._val;
    }


    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        // FIXME: Implement this method to return correct value
        String p = "[";
        for(DNode temp = _front; temp != null; temp = temp._next){
            p += temp._val + (temp != _back ? ", " : "");
        }
        return p + "]";
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
