package com.barclayadunn;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by barclayadunn on 7/6/16 at 3:06 PM.
 */
class Stack {
    Node top;

    Map<Node, Integer> dataMaxMap = new HashMap<Node, Integer>();

    public void push(int data) {
        Node newNode = new Node(data);
        Node prevTop = top;
        newNode.setNext(prevTop);
        top = newNode;
        int max = dataMaxMap.get(prevTop) > data ? dataMaxMap.get(prevTop) : data;
        dataMaxMap.put(top, max);
    }

    public Integer pop() {
        if (top == null) return null;
        Integer data = top.getData();
        top = top.getNext();
        dataMaxMap.remove(top);
        return data;
    }

    public Integer getMax() {
        return dataMaxMap.get(top);
    }
}

class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
        this.next = null;
    }

    public int getData() {
        return data;
    }

    public Node getNext() {
        return next;
    }

    public void setData(int data) {
        this.data = data;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
