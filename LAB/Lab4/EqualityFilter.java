/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {
    private String colName;
    private String match;
    private Table _input;

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        // FIXME: Add your code here.
        this.colName = colName;
        this.match = match;
        this._input = input;
    }

    @Override
    protected boolean keep() {
        // FIXME: Replace this line with your code.
        Table.TableRow m = candidateNext();
        return (m.getValue(_input.colNameToIndex(colName)).equals(match));
    }

    // FIXME: Add instance variables?
}
