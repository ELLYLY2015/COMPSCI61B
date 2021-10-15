/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {
    private Table input;
    private String colName;
    private String ref;

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        // FIXME: Add your code here.
        this.input = input;
        this.colName = colName;
        this.ref = ref;
    }

    @Override
    protected boolean keep() {
        Table.TableRow m = candidateNext();
        return (m.getValue(input.colNameToIndex(colName)).compareTo(ref)  > 0);
    }

    // FIXME: Add instance variables?
}
