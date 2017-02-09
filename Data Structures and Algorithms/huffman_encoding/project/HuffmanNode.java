package project; //TODO: remove package declaration befor submission
/*
 * <Paul Zeng>, <paulmiaozeng@gmail.com>
 * <current date>
 */

import ProblemSet3.programmingProblems.StringNode;

/**
 * Created by Paul on 12/4/2015.
 */
public class HuffmanNode {

    // TODO: change access let to private; and create getters and setters
    int key;         // the key field
    int data;     // the rest of the data item
    HuffmanNode left;       // reference to the left child/subtree
    HuffmanNode right;      // reference to the right child/subtree
    HuffmanNode parent;     // reference to the parent
    HuffmanNode next;

    public HuffmanNode(int key, int data, HuffmanNode left, HuffmanNode right, HuffmanNode parent, HuffmanNode next) {
        this.key = key;
        this.data = data;
        this.left = left;
        this.right = right;
        this.parent = parent;
        this.next = next;
    }

    public HuffmanNode(int key, int data) {
        this(key, data, null, null, null, null);
    }

    public String toString() {
        return "Key: " + key + " Data: " + data;
    }


}
