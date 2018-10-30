package com.barclayadunn.cards;

import java.util.*;

/**
 * User: barclaydunn
 * Date: 6/26/13
 * Time: 2:34 PM
 */
public class Cards2 {

    private static List<Card> deck = new ArrayList<Card>(52);
    private static Map<String, Integer> faceValues = new HashMap<String, Integer>();
    private static Map<Integer, String> valueFaces = new HashMap<Integer, String>();
    private static Map<String, Integer> suitValues = new HashMap<String, Integer>();
    private static Map<Integer, String> valueSuits = new HashMap<Integer, String>();

    public static void main(String [] args) {
        setupFaces();
        setupSuits();

        for (String suit : suitValues.keySet()) {
            for (String face: faceValues.keySet()) {
                deck.add(new Card(face, suit));
            }
        }
        printDeck(deck);

        Set<Card> sortedCards = new TreeSet<Card>(deck);
        printDeck(sortedCards);
    }

    private static void printDeck(Collection<Card> deck) {
        for (Card card : deck) {
            System.out.print(card + " ");
        }
        System.out.println();
    }

    private static void setupSuits() {
        suitValues.put("H", 4);
        suitValues.put("S", 3);
        suitValues.put("D", 2);
        suitValues.put("C", 1);

        valueSuits.put(4, "H");
        valueSuits.put(3, "S");
        valueSuits.put(2, "D");
        valueSuits.put(1, "C");
    }

    private static void setupFaces() {
        faceValues.put("2", 2);
        faceValues.put("3", 3);
        faceValues.put("4", 4);
        faceValues.put("5", 5);
        faceValues.put("6", 6);
        faceValues.put("7", 7);
        faceValues.put("8", 8);
        faceValues.put("9", 9);
        faceValues.put("10", 10);
        faceValues.put("J", 11);
        faceValues.put("Q", 12);
        faceValues.put("K", 13);
        faceValues.put("A", 14);

        valueFaces.put(2, "2");
        valueFaces.put(3, "3");
        valueFaces.put(4, "4");
        valueFaces.put(5, "5");
        valueFaces.put(6, "6");
        valueFaces.put(7, "7");
        valueFaces.put(8, "8");
        valueFaces.put(9, "9");
        valueFaces.put(10, "10");
        valueFaces.put(11, "J");
        valueFaces.put(12, "Q");
        valueFaces.put(13, "K");
        valueFaces.put(14, "A");
    }
}

class Card implements Comparable<Card> {
    private int faceValue;
    private int suitValue;
    private String faceString;
    private String suitString;

    public Card(String faceString, String suitString) {
        this.faceString = faceString;
        this.suitString = suitString;

        switch (faceString.charAt(0)) {
            case('J'):
                faceValue = 11;
                break;
            case('Q'):
                faceValue = 12;
                break;
            case('K'):
                faceValue = 13;
                break;
            case('A'):
                faceValue = 14;
                break;
            default:
                faceValue = Integer.parseInt(faceString);
        }
        switch(suitString.charAt(0)) {
            case('H'):
                suitValue = 4;
                break;
            case('S'):
                suitValue = 3;
                break;
            case('D'):
                suitValue = 2;
                break;
            case('C'):
            default:
                suitValue = 1;
        }
    }

    @Override
    /**
     * suit value is primary, face value secondary
     */
    public int compareTo(Card c2) {
        // descending value of suits (H-S-D-C)
        if (this.suitValue > c2.suitValue) {
            return -1;
        } else if (this.suitValue < c2.suitValue) {
            return 1;
        }
        if (this.faceValue > c2.faceValue) {
            return 1;
        } else if (this.faceValue < c2.faceValue) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(suitString).append(faceString);
        return stringBuilder.toString();
    }
}
