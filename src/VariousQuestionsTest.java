import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * User: barclaydunn
 * Date: Feb 27, 2012
 * Time: 2:28:19 PM
 */
public class VariousQuestionsTest {

    public static void main(String [] args) {

        ArrayList<String> testArrayList = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                testArrayList.add(null);
            } else {
                testArrayList.add("odd number");
            }
        }
        for (String testString : testArrayList) {
//            System.out.println("String: " + testString);
        }

        HashMap<String, String> testHashMap = new HashMap<String, String>();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                testHashMap.put(Integer.toString(i), null);
            } else {
                testHashMap.put(Integer.toString(i), "odd number");
            }
        }
        for (String key : testHashMap.keySet()) {
//            System.out.println("Key: " + key + "; value: " + testHashMap.get(key));
        }

        Hashtable<String, String> testHashtable = new Hashtable<String, String>();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                testHashtable.put(Integer.toString(i), null);
            } else {
                testHashtable.put(Integer.toString(i), "odd number");
            }
        }
        for (String key : testHashtable.keySet()) {
            System.out.println("Key: " + key + "; value: " + testHashtable.get(key));
        }
    }
}
