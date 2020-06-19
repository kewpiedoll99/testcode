package com.barclayadunn.google;

import java.lang.Integer;
import java.util.Iterator;
import java.util.NoSuchElementException;

/*
    Write an iterator that unpacks a run-length encoding. The encoding is an array of integers, where the i'th element tells you how many times to repeat the i+1'th. For example, given the array [3,8,0,9,2,12], the iterator should return "8" the first 3 times it's called, then "12" the next two, then end. Note that it's not returning [8,8,8,12,12] all at once, but one at a time, each time it's pinged. Also note that "0" is a valid repeat number.
        [3,8,0,9,2,12] -> [8,8,8,12,12]
        [3,8,0,9]
*/
public class Unpack {
    public static void main(String [] args) {
        int[] encoding = new int[] {3,8,0,9,2,12};
//        int[] encoding = new int[] {3,8,0,9};
        System.out.print("encoding: ");
        for (int i : encoding) {
            System.out.print(i + " ");
        }
        System.out.println();
        UnpackIterator ui = new UnpackIterator(encoding);
        while (ui.hasNext()) {
            System.out.println(ui.next());
        }
    }
}

class UnpackIterator implements Iterator {
    private int [] array;
    private int counter = 0;
    private int countOfRepeats = 0; // 2
    private int countRepeated = 0; // 2
    private int numberToRepeat = 0; // 12

    UnpackIterator(int [] array) {
        this.array = array;
        if (array != null && array.length > 1) {
            countOfRepeats = array[0];
            numberToRepeat = array[1];
            counter += 2;
        }
    }

    public Integer next() {
        if (hasNext()) {
            if (countRepeated < countOfRepeats) {
                countRepeated++;
            } else {
                // reset
                countOfRepeats = 0;
                while (countOfRepeats == 0 && hasNext()) {
                    countOfRepeats = array[counter++];
                    numberToRepeat = array[counter++];
                    countRepeated = 1;
                }
            }
            return numberToRepeat;
        } else {
            throw new NoSuchElementException();
        }
    }

    // needs to confirm
    public boolean hasNext() {
        // are we still in the repeats
        if (countRepeated < countOfRepeats) {
            return true;
        } else if (array.length > counter && array[counter] > 0) {
            return true;
        } else {
            int local = counter;
            while (array.length > local && array[local] == 0) {
                // check if next set has the right value
                local += 2;
            }
            if (array.length > local && array[local] > 0) {
                counter = local;
                return true;
            }
        }
        return false;
    }
}
