package com.barclayadunn.crackingCodingInterview.arraysStrings;

import java.util.HashMap;
import java.util.Map;

public class Question1 {
    static final String[] testStrings = new String[]{"abcdefghijklmnopqrstuvwxyz","Bobbie Brown","Hello"};

    public static void main(String[] args) {
        for (String str : testStrings) {
            System.out.println("String " + str + (isUnique(str) ? " is " : " is not ") + "unique");
            System.out.println("String " + str + (isUnique2(str) ? " is " : " is not ") + "unique");
        }
    }

    private static boolean isUnique(String str) {
        Map<Character, Boolean> seen = new HashMap<>();
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (seen.containsKey(chars[i])) {
                return false;
            } else {
                seen.put(chars[i], true);
            }
        }
        return true;
    }

    private static boolean isUnique2(String str) {
        char[] chars = quicksort(str.toCharArray(), 0, str.length() - 1);
        char prev = 0;
        for (char c : chars) {
            if (c == prev) {
                return false;
            }
            prev = c;
        }
        return true;
    }


    static char[] quicksort(char[] arr, int first, int last) {
        int index = partition(arr, first, last);
        if (first < index - 1) {
            arr = quicksort(arr, first, index - 1);
        }
        if (index < last) {
            arr = quicksort(arr, index, last);
        }
        return arr;
    }

    static int partition(char[] arr, int left, int right) {
        int pivot = arr[(left + right)/2];
        while (left <= right) {
            while (arr[left] < pivot) {
                left++;
            }
            while (arr[right] > pivot) {
                right--;
            }
            if (left <= right) {
                arr = swap(arr, left, right);
                left++;
                right--;
            }
        }
        return left;
    }

    static char[] swap(char[] arr, int left, int right) {
        char holder = arr[left];
        arr[left] = arr[right];
        arr[right] = holder;
        return arr;
    }
}
