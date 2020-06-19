package com.barclayadunn.leetcode.redundant;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.HashSet;

public class Redundant {
    public static void main(String [] args) {
        int[][] M = new int[][] {{1,2}, {1,3}, {2,3}};
//         int[][] M = new int[][] {{1,2}, {2,3}, {3,4}, {1,4}, {1,5}};
//         System.out.println(findRedundantConnection(M));
        Assert.assertArrayEquals(new int[] {2,3}, findRedundantConnection(M));
    }

    public static int[] findRedundantConnection(int[][] edges) {
        ArrayList<HashSet<Integer>> sets = new ArrayList<>();
        ArrayList<HashSet<Integer>> prevSets = new ArrayList<>();
        HashSet<Integer> set;

        // initialize separate sets; we know each edge is a pair
        for (int i=0; i<edges.length; i++) {
            set = new HashSet<Integer>();
            set.add(edges[i][0]);
            sets.add(set);
            set = new HashSet<Integer>();
            set.add(edges[i][1]);
            sets.add(set);
        }

        prevSets = sets;
        int[] latestRedundant = new int[2];
        // for each in edges, unite sets
        for (int i=0; i<edges.length; i++) {
            sets = uniteXYInSets(edges[i][0], edges[i][1], sets);
            if (sets.size() == prevSets.size()) {
                latestRedundant = edges[i];
            }
            prevSets = sets;
        }
        // at each change, compare resulting sets count with that of previous sets
        // each time they are the same, the set in edges was redundant
        // keep going through all the input sets of edges, return the last
        return latestRedundant;
    }

    static ArrayList<HashSet<Integer>> uniteXYInSets(int x, int y, ArrayList<HashSet<Integer>> setList) {
        HashSet<Integer> setX = null, setY = null;
        // find set containing each of targets to unite
        for (HashSet<Integer> set : setList) {
            // System.out.print("x: " + x + "; y: " + y + "; ");
            // printSet("set", set);
            if (set.contains(x))
                setX = set;
            if (set.contains(y))
                setY = set;
        }
        // printSet("setX", setX);
        // printSet("setY", setY);
        ArrayList<HashSet<Integer>> newSetList = new ArrayList<>();
        for (HashSet<Integer> set : setList) {
            // printSet("set", set);
            if (!set.equals(setX) && !set.equals(setY)) {
                // System.out.println(" is not x or y, adding");
                newSetList.add(set);
            }
        }
        // merge setX and setY and add to newSetList
        HashSet<Integer> setMerge = new HashSet<>();
        setMerge.addAll(setX);
        setMerge.addAll(setY);
        newSetList.add(setMerge);
        // for (int i = 0; i < newSetList.size(); i++) {
            // printSet(i + "", newSetList.get(i));
        // }
        return newSetList;
    }
}
