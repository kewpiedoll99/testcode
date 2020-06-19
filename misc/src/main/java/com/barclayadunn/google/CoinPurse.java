package com.barclayadunn.google;

/*
Input: D = {1, 5, 10, 25}, MAX = 7
        Wrong output #1: Greedy strategy; construct "7" from the coins in D: {2, 1, 0, 0}. This cannot exactly construct the value 3 or 4.
        Wrong output #2: O(1) strategy: simply return MAX pennies: {7, 0, 0, 0}. This is not the smallest set.
        Correct output: {4, 1, 0, 0}
 */
public class CoinPurse {
    public static void main (String [] args) {
        int[] coins = new int[] {1, 5, 10, 25};
        int[] output = makeChange(coins, 7);
        System.out.print("output: ");
        for (int i : output) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    private static int [] makeChange(int[] coinSet, int max) {
        int [] returnSet = new int[coinSet.length];
        // 4, 1, 2, 1
        for (int i = 1; i <= max; i++) {
            if (i <= 4) {
                returnSet[0]++;
            } else if (i < 10 && i % 5 == 0) { // increment nickels
                returnSet[1]++;
            } else if (i < 29 && i % 10 == 0) { // increment dimes
                returnSet[2]++;
            } else if (i > (returnSet[0] + returnSet[1]*5 + returnSet[2]*10 + returnSet[3]*25))
                returnSet[3]++;
        }
        return returnSet;
    }
}
