/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        input = input;
        // FIXME: Add your code here.
        colName1 = colName1;
        colName2 = colName2;
        col1 = input.colNameToIndex(colName1);
        col2 = input.colNameToIndex(colName2);
    }

    @Override
    protected boolean keep() {
        // FIXME: Replace this line with your code.
        Table.TableRow m = candidateNext();
        return (m.getValue(col1).equals(m.getValue(col2)));
    }

    // FIXME: Add instance variables?
    private String colName1;
    private String colName2;
    int col1;
    int col2;
    private Table input;
}
