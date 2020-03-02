package com.barclayadunn.convert;

/**
 * Created by IntelliJ IDEA.
 * User: barclaydunn
 * Date: Jul 20, 2010
 * Time: 9:36:54 AM
 */
public class BinaryConverter {
    public static void main(String[] args) {
//        cast to int in base 2 the result to a char
        String [] binaries = {"01010001", "01110101", "01101001", "01101100", "01110100"};
//        char [] output = new char[binaries.length];
        for (int i = 0; i < binaries.length; i++) {
            System.out.print((char) Integer.parseInt(binaries[i], 2));
        }
        System.out.println("");
    }
}
