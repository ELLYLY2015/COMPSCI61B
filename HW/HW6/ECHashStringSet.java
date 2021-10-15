import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
/** A set of String values.
 *  @author
 */
class ECHashStringSet implements StringSet {
    public ECHashStringSet() {
        _size = 0;
        _table = new LinkedList[5];
    }
    @Override
    public void put(String s) {
        // FIXME
        if (_table.length != 0 && (double) _size / (double) _table.length > 5) {
            resize();
        }
        int index = s.hashCode() % _table.length;
        if (index < 0) {
            index = index & 0x7fffffff % _table.length;
        }
        while (index > _table.length) {
            resize();
        }
        if (_table[index] == null) {
            _table[index] = new LinkedList<>();
        }
        _table[index].add(s);
        _size++;
    }

    @Override
    public boolean contains(String s) {
        int index = s.hashCode() % _table.length;
        if (index < 0) {
            index = index & 0x7fffffff % _table.length;
        }
        if (_table[index] != null && _table[index].contains(s)) {
            return true;
        }
        return false;
    }

    @Override
    public List<String> asList() {
        ArrayList<String> result = new ArrayList<>();
        for (LinkedList<String> ele : _table) {
            if (ele != null) {
                for (String s : ele) {
                    result.add(s);
                }
            }
        }
        return result;
    }
    public void resize() {
        LinkedList<String>[] temp = _table;
        _table = new LinkedList[2 * temp.length];
        _size = 0;
        for (LinkedList<String> ele : temp) {
            if (ele != null) {
                for (String s : ele) {
                    this.put(s);
                }
            }
        }
    }
    private LinkedList<String> [] _table;
    private int _size;
}
