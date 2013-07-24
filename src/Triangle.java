import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * User: barclaydunn
 * Date: 4/2/13
 * Time: 11:35 AM
 *
 * solution to question at http://www.yodlecareers.com/puzzles/triangle.html
 */
public class Triangle {

    static boolean debugRequested = false;

    public static void main(String[] args) {
        if (args.length < 1) {
            error("Usage: java Triangle filename");
            return;
        }

        // referenceMap = original numbers by row & index
        Map<Integer, LinkedList<Long>> referenceMap = new HashMap<Integer, LinkedList<Long>>();
        LinkedList<Long> referenceRow;
        Long rowNumber;
        Integer count = new Integer(0);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            String line;

            while((line = reader.readLine()) != null) {
                referenceRow = new LinkedList<Long>();
                debug(line);
                String [] rowNumberStrings = line.split(" ");
                for (String rowNumberString : rowNumberStrings) {
                    try {
                        rowNumber = Long.parseLong(rowNumberString);
                        referenceRow.add(rowNumber);
                    } catch (NumberFormatException nfe) {
                        error(nfe.getMessage());
                        return;
                    }
                }
                referenceMap.put(count, referenceRow);
                count++;
            }
        } catch (FileNotFoundException fnfe) {
            error(fnfe.getMessage());
            return;
        } catch (IOException ioe) {
            error(ioe.getMessage());
            return;
        }
        int rowCount = count;
        debug("rowCount: " + rowCount);

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j <= i; j++) {
                debug("refmap(" + i + ")(" + j + "): " + referenceMap.get(i).get(j));
            }
        }

//        cumulativeMap = rows best sums with prev rows
        Map<Integer, LinkedList<Long>> cumulativeMap = new HashMap<Integer, LinkedList<Long>>();
        LinkedList<Long> cumulativeRow;

        // iterate through referenceMap rows
        for (int i = 0; i < rowCount; i++) {
            cumulativeRow = new LinkedList<Long>();

            // iterate through referenceMap referenceRow values
            for (int j = 0; j <= i; j++) {
                debug("i: " + i + "; j: " + j);
                Long refRowIndexValue = referenceMap.get(i).get(j);
                debug("refRowIndexValue: " + refRowIndexValue);
                boolean cumePrevRowExists = (i-1 >= 0);
                debug("cumePrevRowExists: " + cumePrevRowExists);
                boolean cumePrevRowPrevIndexExists = (j-1 >= 0);
                debug("cumePrevRowPrevIndexExists: " + cumePrevRowPrevIndexExists);
                boolean cumePrevRowSameIndexExists = (j <= i-1);
                debug("cumePrevRowSameIndexExists: " + cumePrevRowSameIndexExists);
                long cumePrevRowPrevIndexValue = cumePrevRowExists && cumePrevRowPrevIndexExists ? cumulativeMap.get(i-1).get(j-1) : 0;
                debug("cumePrevRowPrevIndexValue: " + cumePrevRowPrevIndexValue);
                long cumePrevRowSameIndexValue = cumePrevRowExists && cumePrevRowSameIndexExists ? cumulativeMap.get(i-1).get(j) : 0;
                debug("cumePrevRowSameIndexValue: " + cumePrevRowSameIndexValue);
                long cumeRowIndexValue = cumePrevRowPrevIndexValue > cumePrevRowSameIndexValue ?
                        refRowIndexValue + cumePrevRowPrevIndexValue :
                        refRowIndexValue + cumePrevRowSameIndexValue;
                debug("cumeValue: " + cumeRowIndexValue);
                cumulativeRow.add(cumeRowIndexValue);
            }
            cumulativeMap.put(i, cumulativeRow);
        }

        // now i want the highest value in cumulativeMap.get(rowCount-1)
        LinkedList<Long> lastCumulativeRow = cumulativeMap.get(rowCount - 1);
        Long [] topValues = new Long[lastCumulativeRow.size()];
        topValues = lastCumulativeRow.toArray(topValues);
        Arrays.sort(topValues);
        System.out.println(topValues[topValues.length - 1]);
    }

    static void error(String output) {
        System.err.println("ERROR: " + output);
    }

    static void debug(String output) {
        if (debugRequested) {
            System.out.println("debug: " + output);
        }
    }
}
