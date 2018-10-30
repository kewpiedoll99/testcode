package com.barclayadunn.trees;

/**
 * Created by barclayadunn
 * on Thu 6/23/16.
 */
public class BinarySearchTree {
    // Root node pointer. Will be null for an empty tree.
    private Node root;

    /**
     * Returns true if the given target is in the binary tree.
     * Uses a recursive helper.
     */
    public boolean lookup(int data) {
        return (lookup(root, data));
    }

    /**
     * Given a binary tree, return true if a node
     * with the target data is found in the tree. Recurs
     * down the tree, chooses the left or right
     * branch by comparing the target to each node.
     */
    boolean lookup(Node node, int target) {
        // 1. Base case == empty tree
        // in that case, the target is not found so return false
        if (node == null) {
            return false;
        } else {
            // 2. see if found here
            if (target == node.data)
                return true;
            else {
                // 3. otherwise recur down the correct subtree
                if (target < node.data)
                    return (lookup(node.left, target));
                else
                    return (lookup(node.right, target));
            }
        }
    }

    /**
     * Inserts the given data into the binary tree.
     * Uses a recursive helper.
     */
    public void insert(int data) {
        root = insert(root, data);
    }

    /**
     * Given a binary search tree and a number, inserts a new node
     * with the given number in the correct place in the tree.
     * Returns the new root pointer which the caller should
     * then use (the standard trick to avoid using reference
     * parameters).
     */
    Node insert(Node node, int data) {
        // check if node is null
        if (node == null)
            return new Node(data);
            // look up if data is already in the tree
        else {
//            if (data == node.data)
//                return node;
//            else
//            if (data < node.data)
// WHY NOT THE ABOVE INSTEAD OF THE FOLLOWING SINGLE LINE?
            if (data <= node.data)
                node.left = insert(node.left, data);
            else
                node.right = insert(node.right, data);
        }
        return node;
    }

    public int size() {
        return countNodes(root);
    }

    int countNodes(Node node) {
        if (node == null)
            return 0;
        else
            return 1 + countNodes(node.left) + countNodes(node.right);
    }

    public int maxDepth() {
        return maxDepth(root);
    }

    int maxDepth(Node node) {
        if (node == null)
            return 0;
        else
            return 1 + Math.max(maxDepth(node.left), maxDepth(node.right));
    }

    public int minValue() {
        if (root == null)
            throw new NullPointerException("Root must not be null to calculate min value.");
        return minValue(root);
    }

    int minValue(Node node) {
        if (node.left != null)
            return minValue(node.left);
        else
            return node.data;
    }
}
