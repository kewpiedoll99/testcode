package com.barclayadunn.convert;

/**
 * User: barclaydunn
 * Date: Jul 30, 2010
 * Time: 10:34:43 AM
 *
 * http://stackoverflow.com/questions/141525/absolute-beginners-guide-to-bit-shifting
 * Supposedly, shifting to the right (>>>) is equivalent to division by powers of 2.
 * The answers in the output do not bear that out, unless that rule is only for positive operands.
 * The answers in the output for positive operands do bear it out.
 */
public class Bits3 {

// shift all the bits in x (in binary form) to the right by y
// without sign extension using >>>
    public static void main(String args[]) {
        System.out.println(" >>> operator");

        int [] testInts = {-1294765227, -5, -1, 0, 101, 146, 172, 1401301217};
        int [] testShifts = {0, 1, 2, 4, 8, 16, 31, 32};

        for (int x : testInts) {
            // x in binary form
            System.out.println(x + " in binary form is " + Integer.toBinaryString(x));

            for (int y : testShifts) {
                // y in binary form
                System.out.println(y + " in binary form is " + Integer.toBinaryString(y));

                int z = x >>> y;
                System.out.println(x + " >>> " + y + " = " + z);

                // x >>> y in binary form
                System.out.println(x + " >>> " + y + " in binary form is " + Integer.toBinaryString(z));
            }
            System.out.println();
        }
    }
}
