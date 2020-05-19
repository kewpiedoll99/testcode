package com.pronto.pig;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

/**
 * User: barclaydunn
 * Date: 6/27/13
 * Time: 3:57 PM
 */
public class LevenshteinDistance extends EvalFunc<String> {

    private TupleFactory tupleFactory = TupleFactory.getInstance();

    @Override
    public String exec(Tuple tuple) throws IOException {
        // If tuple is null
        if (tuple == null) {
            throw new IOException("Null tuple.");
        }
        try {
            Object values = tuple.get(0);
            if (values instanceof DataBag) {
//                return "DataBag";
                DataBag bagValues = (DataBag) values;
                Iterator it = bagValues.iterator();
//                StringBuilder sb = new StringBuilder("databag contains: ");
                if (it.hasNext()) {
//                    sb.append(it.next().toString()).append("|");
                    String bagAsString = it.next().toString();
                    bagAsString = bagAsString.substring(1, (bagAsString.length() - 1));
                    String [] townCountyEtc = bagAsString.split(",");
                    int levDist = getLevenshteinDistance(townCountyEtc[0], townCountyEtc[1]);
//                    return Integer.toString(levDist);
                    BigDecimal bdLevDist = new BigDecimal(levDist);
                    BigDecimal bdTownLength = new BigDecimal(townCountyEtc[0].length());
                    BigDecimal bdCountyLength = new BigDecimal(townCountyEtc[1].length());
                    BigDecimal bdLongerStringLength = bdTownLength.compareTo(bdCountyLength) > 0 ? bdTownLength : bdCountyLength;
                    BigDecimal levDistAsPct = bdLevDist.divide(bdLongerStringLength, BigDecimal.ROUND_HALF_EVEN);
                    return (levDistAsPct.multiply(new BigDecimal(100))).toString();
                } else {
                    return "nothing next";
                }
//                return sb.toString();
            } else if (values instanceof Map) {
                return "Map";
            } else if (values instanceof String) {
                return "String";
            } else {
                return "neither";
            }
//            if (values instanceof DataBag && ((DataBag)values).size() > 1) {
//                Iterator it = bagValues.iterator();
//                if (it.hasNext()) {
//                    Tuple t = (Tuple) it.next();
//                    String town = (String) t.get(0);
//                    String county = (String) t.get(1);
//
//                    int levDist = getLevenshteinDistance(town, county);
//                    return Integer.toString(levDist);
//                }
//            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }


    /**
     * outright stolen from
     * http://www.java2s.com/Code/Java/Data-Type/FindtheLevenshteindistancebetweentwoStrings.htm
     *
     * @param s
     * @param t
     * @return
     */
    int getLevenshteinDistance(String s, String t) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        /*
          The difference between this impl. and the previous is that, rather
          than creating and retaining a matrix of size s.length()+1 by t.length()+1,
          we maintain two single-dimensional arrays of length s.length()+1.  The first, d,
          is the 'current working' distance array that maintains the newest distance cost
          counts as we iterate through the characters of String s.  Each time we increment
          the index of String t we are comparing, d is copied to p, the second int[].  Doing so
          allows us to retain the previous cost counts as required by the algorithm (taking
          the minimum of the cost count to the left, up one, and diagonally up and to the left
          of the current cost count being calculated).  (Note that the arrays aren't really
          copied anymore, just switched...this is clearly much better than cloning an array
          or doing a System.arraycopy() each time  through the outer loop.)

          Effectively, the difference between the two implementations is this one does not
          cause an out of memory condition when calculating the LD over two very large strings.
        */

        int n = s.length(); // length of s
        int m = t.length(); // length of t

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        if (n > m) {
            // swap the input strings to consume less memory
            String tmp = s;
            s = t;
            t = tmp;
            n = m;
            m = t.length();
        }

        int p[] = new int[n+1]; //'previous' cost array, horizontally
        int d[] = new int[n+1]; // cost array, horizontally
        int _d[]; //placeholder to assist in swapping p and d

        // indexes into strings s and t
        int i; // iterates through s
        int j; // iterates through t

        char t_j; // jth character of t

        int cost; // cost

        for (i = 0; i<=n; i++) {
            p[i] = i;
        }

        for (j = 1; j<=m; j++) {
            t_j = t.charAt(j-1);
            d[0] = j;

            for (i=1; i<=n; i++) {
                cost = s.charAt(i-1)==t_j ? 0 : 1;
                // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
                d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]+cost);
            }

            // copy current distance counts to 'previous row' distance counts
            _d = p;
            p = d;
            d = _d;
        }

        // our last action in the above loop was to switch d and p, so p now
        // actually has the most recent cost counts
        return p[n];
    }
}
