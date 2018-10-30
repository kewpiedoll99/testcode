package com.barclayadunn.regex;

/**
 * User: barclaydunn
 * Date: 6/18/12
 * Time: 11:13 AM
 */
public class TrimTest {

    public static void main(String... args) throws Exception {

//        String testString = "\ntest-test-test";
//        String testString = "\rtest-test-test";
//        String testString = "test-test-test\n";
        String testString = "test-test-test\r";

        System.out.println("teststring:" + testString + ":endofteststring");

        String output = testString.trim();

        System.out.println("output:" + output + ":endofoutput");
    }
}
