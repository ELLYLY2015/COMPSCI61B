/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {
    private Table input;
    private String colName;
    private String subStr;

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        // FIXME: Add your code here.
        this.input = input;
        this.colName = colName;
        this.subStr = subStr;
    }

    @Override
    protected boolean keep() {
        // FIXME: Replace this line with your code.
        Table.TableRow m = candidateNext();
        return (m.getValue(input.colNameToIndex(colName)).contains(subStr));
    }

    // FIXME: Add instance variables?
}
