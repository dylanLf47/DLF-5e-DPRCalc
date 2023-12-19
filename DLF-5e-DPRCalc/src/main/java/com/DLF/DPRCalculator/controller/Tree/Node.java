package com.DLF.DPRCalculator.controller.Tree;

public class Node {
    private double percentValue;
    private double hit;
    private Node parent;
    private Node left;
    private Node right;

    public Node(Node parent) {
        this.parent = parent;
    }

    public Node(double percentValue, double hit, Node parent) {
        this.percentValue = percentValue;
        this.hit = hit;
        this.parent = parent;
        this.right = null;
        this.left = null;
    }
    public Node left() {
        return left;
    }
    public Node right() {
        return right;
    }
    public Node parent() {
        return parent;
    }
    public void setLeft(Node lNode) {
        this.left = lNode;
    }
    public void setRight(Node rNode) {
        this.right = rNode;
    }

    public double getPercentValue() {
        return percentValue;
    }

    public double getHit() {
        return hit;
    }

    public double totalValue() {
        return percentValue * hit;
    }
}
