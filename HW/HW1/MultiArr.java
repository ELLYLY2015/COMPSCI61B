/** Multidimensional array 
 *  @author Zoe Plaxco
 */

public class MultiArr {

    /**
    {{“hello”,"you",”world”} ,{“how”,”are”,”you”}} prints:
    Rows: 2
    Columns: 3
    
    {{1,3,4},{1},{5,6,7,8},{7,9}} prints:
    Rows: 4
    Columns: 4
    */
    public static void printRowAndCol(int[][] arr) {
        //TODO: Your code here!
        System.out.println("Row " + arr.length);
        System.out.println("Columns: " + arr[0].length);
    } 

    /**
    @param arr: 2d array
    @return maximal value present anywhere in the 2d array
    */
    public static int maxValue(int[][] arr) {
        int Max = arr[0][0];
        for(int i = 0; i < arr.length; i++)
            for(int j : arr[i])
            {
                if(Max < j)
                    Max = j;
            }
        return Max;
    }

    /**Return an array where each element is the sum of the 
    corresponding row of the 2d array*/
    public static int[] allRowSums(int[][] arr) {
        //TODO: Your code here!!
        int[] result = new int[arr.length];
        for(int i = 0; i < arr.length; i++)
            for(int j : arr[i])
                result[i] += j;
        return result;
    }
}