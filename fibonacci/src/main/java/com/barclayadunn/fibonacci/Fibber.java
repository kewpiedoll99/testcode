package com.barclayadunn.fibonacci;

import java.util.LinkedList;

/**
 * User: barclayadunn
 * Date: 1/28/14
 * Time: 2:46 PM
 *
 * 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, ...
 */
public class Fibber {

    private static int SEED_ZERO = 0;
    private static int SEED_ONE = 1;


    public static void main(String [] args) {

        LinkedList<Integer> sequence = new LinkedList<Integer>();

        sequence.add(SEED_ZERO);
        sequence.add(SEED_ONE);

        for (int i = 0; i < 20; i++) {
            sequence = addNextNumberToSequence(sequence);
        }

        printSequence(sequence);
    }

    private static void printSequence(LinkedList<Integer> sequence) {
        for (int i : sequence) {
            System.out.print(i + ",");
        }
        System.out.println("...");
    }

    private static LinkedList<Integer> addNextNumberToSequence(LinkedList<Integer> sequence) {
        LinkedList<Integer> workableSequenceCopy = (LinkedList<Integer>) sequence.clone();
        int previous, current, next, index;
        current = workableSequenceCopy.getLast();
        index = workableSequenceCopy.size() - 1;
        previous = workableSequenceCopy.get(index - 1);
        next = getNextNumber(previous, current);
        workableSequenceCopy.add(next);
        return workableSequenceCopy;
    }

    private static int getNextNumber(int previous, int current) {
        return previous + current;
    }
}
