package com.barclayadunn.leetcode.friendcircles;

import java.util.ArrayList;
import java.util.HashSet;

public class FriendCircles {
    public static void main(String [] args) {
        int[][] M = new int[][] {{1,1,0}, {1,1,0}, {0,0,1}};
        System.out.println(findCircleNum(M));
    }

    public static int findCircleNum(int[][] M) {
        ArrayList<HashSet<Integer>> sets = new ArrayList<>();
        HashSet<Integer> set;
        // for each row in M
        // make a set of the row index
        // ==> separate sets for all rows.
        for (int i = 0; i < M.length; i++) {
            set = new HashSet<Integer>();
            set.add(i);
            sets.add(set);
        }

        // for each row in M
            // ignoring the self index,
            // if any other index is 1,
                // merge this and that set
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[i].length; j++) {
                if (i == j)
                    continue;
                if (M[i][j] == 1) {
                    sets = uniteXYInSets(i, j, sets);
                }
            }
        }

        // return count of sets
        return sets.size();
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

    static void printSet(String name, HashSet<Integer> set) {
        System.out.print(name + ":");
        for (int i : set) {
            System.out.print(i);
        }
        System.out.println();
    }
}
