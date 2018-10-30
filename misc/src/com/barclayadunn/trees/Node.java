package com.barclayadunn.trees;

/**
 * Created by barclayadunn
 * on Thu 6/23/16.
 */
public class Node {
    public int data;
    public Node left;
    public Node right;

    public Node(int data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }
}
