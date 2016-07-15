package com.barclayadunn;

import java.util.Arrays;
import java.util.List;

/**
 * Created by barclayadunn
 * 7/15/16
 * 3:26 PM
 *
 * Technical phone screen with Peloton Cycle, 2:30-3:30pm 7/15/16
 *
 * "pelotoncycle" => "peloton cycle"
 * "fatcat" => "fat cat"
 * "bananacherry" => "banana cherry"
 * "amountain" => "a mountain"
 * "apple" => "apple"
 * "thefatcat" => "the fat cat"
 * isWord(string) => T/F isWord("peloton") -> True
 */
public class WordParser {

    static List<String> input = Arrays.asList("pelotoncycle", "fatcat", "thefatcat", "apple", "bananacherry", "amountain");
    static List<String> dictionary = Arrays.asList("peloton", "cycle", "fat", "cat", "ban", "a", "an", "mountain", "banana", "apple", "the", "cherry");

    public static void main(String [] args) {
        for (String s : input) {
            System.out.println(s + " => " + parseWords(s));
        }
    }

    public static Boolean isWord(String word) {
        return dictionary.contains(word);
    }

    /* recursive */
    static String parseWords(String s) {
        String output = "";
        String candidate = "";
        String remainder;
        String remainderWords;
        int count = 0;
        for (char c : s.toCharArray()) {
            candidate += c;
            remainder = (count+1 <= s.length()) ? s.substring(count+1) : "";
            if (isWord(candidate)) {
                remainderWords = parseWords(remainder);
                if (stringLengthWithoutSpaces(remainderWords) == remainder.length()) {
                    output = candidate + " " + remainderWords;
                }
            }
            count++;
        }
        return output;
    }

    static int stringLengthWithoutSpaces(String s) {
        int count = 0;
        for (char c : s.toCharArray()) {
            if (c != ' ')
                count++;
        }
        return count;
    }
}
