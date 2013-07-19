/**
 * User: barclaydunn
 * Date: 9/25/12
 * Time: 2:50 PM
 */
public class ConvertFromWindows1252ToIso8859 {
    /*
    * See: http://en.wikipedia.org/wiki/Windows-1252
    * http://www.unicode.org/Public/MAPPINGS/VENDORS/MICSFT/WindowsBestFit/bestfit1252.txt
    * http://download-llnw.oracle.com/javase/tutorial/i18n/text/convertintro.html
    *
    */

    /**
     *  According to the article at http://en.wikipedia.org/wiki/Windows-1252 there
     *  are 27 non-standard unicode mappings used by Microsoft's Windows-1252 character encoding.
     *  This method can replaces a subset of those with characters that are can store properly in a
     *  database that uses ISO-8859-1 character encoding, for example an Oracle 10g database created
     *  using its character encoding defaults.
     * @param sbOriginal
     */
    public static void windows1252ToIso8859(StringBuilder sbOriginal)  {
        if (null==sbOriginal) {
            return;
        }
        for (int isb = 0; isb < sbOriginal.length(); isb++)  {
            int origCharAsInt = (int) sbOriginal.charAt(isb);
            switch (origCharAsInt) {

                case ((int)'\u2018'):  sbOriginal.setCharAt(isb, '\''); break;  // left single quote
                case ((int)'\u2019'):  sbOriginal.setCharAt(isb, '\''); break;  // right single quote
                case ((int)'\u201A'):  sbOriginal.setCharAt(isb, '\''); break;  // lower quotation mark

                case ((int)'\u201C'):  sbOriginal.setCharAt(isb, '"'); break;  // left double quote
                case ((int)'\u201D'):  sbOriginal.setCharAt(isb, '"'); break;  // right double quote
                case ((int)'\u201E'):  sbOriginal.setCharAt(isb, '"'); break;  // double low quotation mark

                case ((int)'\u2039'):  sbOriginal.setCharAt(isb, '\''); break;  // Single Left-Pointing Quotation Mark
                case ((int)'\u203A'):  sbOriginal.setCharAt(isb, '\''); break;  // Single right-Pointing Quotation Mark

                case ((int)'\u02DC'):  sbOriginal.setCharAt(isb, '~'); break;  // Small Tilde

                case ((int)'\u2013'):  sbOriginal.setCharAt(isb, '-'); break;  // En Dash
                case ((int)'\u2014'):  sbOriginal.setCharAt(isb, '-'); break;  // EM Dash

                default: break;
            }
        }
    }

    public static String windows1252ToIso8859(String strOriginal)  {
        if (null==strOriginal) {
            return null;
        }
        StringBuilder sbOrig = new StringBuilder(strOriginal);
        windows1252ToIso8859(sbOrig);
        return sbOrig.toString();
    }

    /*
    * Sample output:
    * original         String:  |“Now” ‘is’ ˜the –time— ‹for›|
    * converted        String:  |"Now" 'is' ~the -time- 'for'|
    * original  StringBuilder:  |“Now” ‘is’ ˜the –time— ‹for›|
    * converted StringBuilder:  |"Now" 'is' ~the -time- 'for'|
    */
    public static void main(String[] args) {
//        String strToTest = ((null==args) || (args.length!=1)) ? "“Now” ‘is’ ˜the –time— ‹for›" : args[0];
        String strToTest = ((null==args) || (args.length!=1)) ? "Customers’ lives blah blah blah, saying “One day, Alice”" : args[0];
        String saveStrToTest = new String(strToTest);
        System.out.println("original         String:  |" + strToTest + "|");
        System.out.println("converted        String:  |" + windows1252ToIso8859(strToTest) + "|");
        StringBuilder sb = new StringBuilder(saveStrToTest);
        System.out.println("original  StringBuilder:  |" + sb.toString() + "|");
        windows1252ToIso8859(sb);
        System.out.println("converted StringBuilder:  |" + sb.toString() + "|");
    }
}
