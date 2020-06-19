/*
 * author: Piotr Andrzejewski
 */
package com.kikd.lista2;

import java.util.Map;

public class Node {
    private Node left;
    private Node right;
    private Node parent;
    private int weight;
    private byte symbol;
    private int index;

    /**
     * Constructor for NYT(root) node
     */
    public Node(int index) {
        this.index = index;

        this.weight = 0;
    }

    /**
     * Constructor for NYT node
     */
    public Node(Node parent, int index) {
        this.parent = parent;
        this.index = index;

        this.weight = 0;
    }

    /**
     * Constructor for leaf node (other than NYT)
     */
    public Node(Node parent, int index, byte symbol, Map<Byte, Node> symbolMapping) {
        this.parent = parent;
        this.index = index;
        this.symbol = symbol;

        symbolMapping.put(symbol, this);
        this.weight = 0;
    }

    /**
     * Constructor for internal node
     */
    public Node(Node parent, Node left, Node right, int index) {
        this.parent = parent;
        this.left = left;
        this.right = right;
        this.index = index;

        this.weight = left.getWeight() + right.getWeight();
    }

    public boolean isInternal() {
        return left != null || right != null;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    public boolean isRightChild() {
        if (parent != null) {
            return parent.right == this;
        }
        return false;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getWeight() {
        return weight;
    }

    public void incrementWeight() {
        this.weight++;
    }

    public byte getSymbol() {
        return symbol;
    }

    public void setSymbol(byte symbol) {
        this.symbol = symbol;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
