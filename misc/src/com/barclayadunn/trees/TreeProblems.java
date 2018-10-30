package com.barclayadunn.trees;

/**
 * Created by barclayadunn
 * on Thu 6/23/16.
 */
public class TreeProblems {

    private static BinarySearchTree binarySearchTree;

    public static void main (String [] args) {
        build123();
        buildBinaryTree();
        System.out.println("count: " + binarySearchTree.size());
        System.out.println("maxDepth: " + binarySearchTree.maxDepth());
        System.out.println("minValue: " + binarySearchTree.minValue());
        buildBinaryTree2();
        System.out.println("minValue: " + binarySearchTree.minValue());
    }

    /**
     * Make this binary search tree
     *         2
     *        / \
     *       1   3
     */
    static void build123() {
        BinarySearchTree bt = new BinarySearchTree();
//        IntStream.range(1, 4).forEach(bt::insert); this makes a tree 1 \ 2 \ 3, not 2 / 1 \ 3
        bt.insert(2);
        bt.insert(1);
        bt.insert(3);
    }

    private static void buildBinaryTree() {
        binarySearchTree = new BinarySearchTree();
        binarySearchTree.insert(20);
        binarySearchTree.insert(15);
        binarySearchTree.insert(25);
        binarySearchTree.insert(10);
        binarySearchTree.insert(18);
        binarySearchTree.insert(16);
        binarySearchTree.insert(19);
        binarySearchTree.insert(17);
    }

    private static void buildBinaryTree2() {
        binarySearchTree = new BinarySearchTree();
        binarySearchTree.insert(20);
        binarySearchTree.insert(-15);
        binarySearchTree.insert(25);
        binarySearchTree.insert(10);
        binarySearchTree.insert(-18);
        binarySearchTree.insert(-16);
        binarySearchTree.insert(19);
        binarySearchTree.insert(17);
    }
}
