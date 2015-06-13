package com.barclayadunn.fibonacci;

/**
 * User: barclayadunn
 * Date: 1/28/14
 * Time: 3:18 PM
 */
public class RecursiveFibber {

    public static void main(String [] args) {
        Integer [] fibs = new Integer [20];

        fibs = getNextFibonacci(20, 0, 1);

        for (int i : fibs) {
            System.out.print(i + ",");
        }
        System.out.println("...");
    }

    private static Integer [] getNextFibonacci(int timesToCall, int previous, int current) {
        if (timesToCall == 1)
            return new Integer[] {current};
        else
            return getNextFibonacci(timesToCall - 1, current, previous + current);
    }
}
