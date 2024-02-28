package com.DLF.DPRCalculator.controller.Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class BinaryTree {
    private Node root;
    private double successChance;
    private double successChanceAdv;
    private double missChance;
    private double missChanceAdv;
    private double standardHit;
    private double standardHitAdv;
    public double totalTreeValue = 0.0;
    public List<Double> crusherList = new ArrayList<Double>();

    /**
     * Binary tree constructor without nodes
     */
    public BinaryTree() {
        this.root = null;
    }

    /**
     * Build binary tree
     * @param root root of binary tree
     * @param successChance chance of hit without advantage
     * @param successChanceAdv chance of hit with advantage
     * @param missChance chance of miss without advantage
     * @param missChanceAdv chance of miss with advantage
     * @param standardHit average damage without advantage
     * @param standardHitAdv average damage with advantage
     */
    public BinaryTree(Node root, double successChance, double successChanceAdv,
                      double missChance, double missChanceAdv, double standardHit, double standardHitAdv) {
        this.root = root;
        this.successChance = successChance;
        this.successChanceAdv = successChanceAdv;
        this.missChance = missChance;
        this.missChanceAdv = missChanceAdv;
        this.standardHit = standardHit;
        this.standardHitAdv = standardHitAdv;
        totalTreeValue += standardHit;
    }

    /**
     * Inserts a new node into the binary tree. Value and position of node
     * is dependent on values and positioning of previous parent nodes
     * @param temp starting node when inserting into tree
     * @param missChance chance of miss without advantage
     * @param missChanceAdv chance of miss with advantage
     * @param successChance chance of hit without advantage
     * @param successChanceAdv chance of hit with advantage
     * @param standardHit average damage without advantage
     * @param standardHitAdv average damage with advantage
     * @param crusher is the tree factoring in crusher?
     * @return value of inserted node
     */
    public double insert(Node temp, double missChance, double missChanceAdv, double successChance, double successChanceAdv,
                         double standardHit, double standardHitAdv, boolean crusher) {

        double nodeTotalValue = 0.0;

        if (this.root == null) {
            this.root = temp;
            nodeTotalValue =  standardHit;
        }
        else {
            Queue<Node> q = new LinkedBlockingDeque<Node>();
            q.add(temp);

            while (!q.isEmpty()) {
                temp = q.peek();
                q.remove();
                if (temp.left() == null) {
                    if (temp.parent() != null && temp.parent().right().equals(temp)) {
                        temp.setLeft(new Node(temp.getPercentValue() * missChanceAdv, standardHit, temp));
                        nodeTotalValue = temp.getPercentValue() * missChanceAdv * standardHit;
                        break;
                    } else {
                        temp.setLeft(new Node(temp.getPercentValue() * missChance, standardHit, temp));
                        nodeTotalValue = temp.getPercentValue() * missChance * standardHit;
                        break;
                    }
                } else {
                    q.add(temp.left());
                }
                if (temp.right() == null) {
                    if (temp.parent() != null && temp.parent().right().equals(temp)) {
                        temp.setRight(new Node(temp.getPercentValue() * successChanceAdv, standardHitAdv, temp));
                        nodeTotalValue = temp.getPercentValue() * successChanceAdv * standardHitAdv;
                        if (crusher) {
                            crusherList.add(temp.getPercentValue() * (1 - missChanceAdv - successChanceAdv) * standardHitAdv);
                        }
                        break;
                    } else {
                        temp.setRight(new Node(temp.getPercentValue() * successChance, standardHitAdv, temp));
                        nodeTotalValue = temp.getPercentValue() * successChance * standardHitAdv;
                        if (crusher) {
                            crusherList.add(temp.getPercentValue() * (1 - missChance - successChance) * standardHitAdv);
                        }
                        break;
                    }
                } else {
                    q.add(temp.right());
                }
            }
        }
        return nodeTotalValue;
    }

    /**
     * Get root
     * @return root
     */
    public Node root() {
        return root;
    }

    /**
     * List of crusher values
     * @return crusherList
     */
    public List<Double> getCrusherList() {
        return crusherList;
    }

    /**
     * Sets crusherList
     * @param newCrusherList new list to set crusherList to
     */
    public void setCrusherList(List<Double> newCrusherList) {
        crusherList = newCrusherList;
    }
}
