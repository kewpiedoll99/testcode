public class WrongCodeTest {

    /**
    public static void main(String args[]) throws Exception {

        int firstInt;
        String firstString = null;

        firstInt = myParseInt(firstString);

        return firstInt;

        System.out.println("The string " + firstString + " is parsed as " firstInt);
    }

    static int myParseInt(String s) {

        try {
            return Integer.parseInt(s);
        }
    }
     */
}

/**
The main method, which has void result type, cannot return a value.
Line 12 can never be reached since it is after the return.
Line 12 is missing a + before firstInt.
The try is missing a catch in the myParseInt method.
The method myParseInt does not return a value if the try fails.
 */
