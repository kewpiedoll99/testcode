package com.barclayadunn.cards;

import java.util.*;

/**
 * User: barclaydunn
 * Date: 6/26/13
 * Time: 10:50 AM
 */
public class Cards {

    private static List<String> deck = new ArrayList<String>(52);
    private static List<String> shuffledDeckValues = new ArrayList<String>(52);
    private static Map<Integer, String> shuffledDeck = new HashMap<Integer, String>(52);
    private static Map<String, List<String>> suitSortedDecks = new HashMap<String, List<String>>();
    private static List<String> sortedDeck = new LinkedList<String>();
    private static String [] SUITS = new String[]{"H", "S", "D", "C"};
    private static String [] FACES = new String[]{"J", "Q", "K", "A"};
    private static List<String> SORTED_REF_VALUES = new LinkedList<String>();

    public static void main(String [] args) {
        if (args.length != 1) {
            System.out.println("Usage: Cards <randomstring>");
            System.exit(-1);
        }

        // setup reference values for sorting
        setupSortedRefValues();

        // fill the deck with values
        // H=hearts, S=spades, D=diamonds, C=clubs
        for (int i = 2; i < 11; i++) {
            for (String suit : SUITS) {
                deck.add(i + suit);
            }
        }
        for (String face : FACES) {
            for (String suit : SUITS) {
                deck.add(face + suit);
            }
        }
        printDeck(deck);

        shuffle(args[0]);

        for (String card : shuffledDeck.values()) {
            shuffledDeckValues.add(card);
        }
        printDeck(shuffledDeckValues);

        sort();

        printDeck(sortedDeck);
    }

    private static void setupSortedRefValues() {
        SORTED_REF_VALUES.add("2");
        SORTED_REF_VALUES.add("3");
        SORTED_REF_VALUES.add("4");
        SORTED_REF_VALUES.add("5");
        SORTED_REF_VALUES.add("6");
        SORTED_REF_VALUES.add("7");
        SORTED_REF_VALUES.add("8");
        SORTED_REF_VALUES.add("9");
        SORTED_REF_VALUES.add("10");
        SORTED_REF_VALUES.add("J");
        SORTED_REF_VALUES.add("Q");
        SORTED_REF_VALUES.add("K");
        SORTED_REF_VALUES.add("A");
    }

    private static void sort() {
        for (String suit : SUITS) {
            List<String> sortedSuitDeck = new ArrayList<String>();
            for (String card : deck) {
                if (card.contains(suit)) {
                    sortedSuitDeck.add(card);
                }
            }
            suitSortedDecks.put(suit, sortedSuitDeck);
        }
        for (List<String> suitDeck : suitSortedDecks.values()) {
            for (String refCard : SORTED_REF_VALUES) {
                for (String card : suitDeck) {
                    if (card.contains(refCard)) {
                        sortedDeck.add(card);
                    }
                }
            }
        }
    }

    private static void shuffle(String arg) {
        Random random = new Random();
        random.setSeed(arg.hashCode());

        for (String card : deck) {
            int index = random.nextInt();
            while (shuffledDeck.get(index) != null) {
                index = random.nextInt();
            }
            shuffledDeck.put(index, card);
        }
    }

    private static void printDeck(List<String> deck) {
        for (String card : deck) {
            System.out.print(card + " ");
        }
        System.out.println();
    }
}

