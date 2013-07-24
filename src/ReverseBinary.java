import java.util.ArrayList;
import java.util.Scanner;

/**
 * User: barclaydunn
 * Date: 3/22/13
 * Time: 12:12 PM
 */
public class ReverseBinary {

    public static void main(String[] args) {

        int input = 0;
        try {
            Scanner sc = new Scanner(System.in);
            input = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Unacceptable input. Please try again with a positive integer.");
            return;
        }
        if (input < 1) {
            System.err.println("Unacceptable input. Please try again with a positive integer.");
            return;
        }

        reverseBinary(input);
    }

    private static void reverseBinary(int input) {
        String binOfInput = Integer.toBinaryString(input);
//        System.out.println(input + " in binary form is " + binOfInput);

        ArrayList<Character> reversedBinAL = new ArrayList<Character>(binOfInput.length());

        for (char x : binOfInput.toCharArray()) {
            reversedBinAL.add(0, x);
        }

        StringBuffer sb = new StringBuffer();
        for (char x : reversedBinAL) {
            sb.append(x);
        }
        String reversedBin = sb.toString();
//        System.out.println("reversed binary is           " + reversedBin);

        int output = Integer.parseInt(reversedBin, 2);
//        System.out.println("Reversed binary in int form is: " + output);
        System.out.println(output);
    }
}
