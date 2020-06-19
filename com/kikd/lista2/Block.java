/*
 * author: Piotr Andrzejewski
 */
package com.kikd.lista2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Block {
    private List<Node> nodes;

    private Node leader;

    private int weight;

    public Block(Node node) {
        this.nodes = new ArrayList<>(List.of(node));
        this.leader = node;
        this.weight = node.getWeight();
    }

    public Block() {
        this.nodes = new ArrayList<>();
    }

    public void addNode(Node node) {
        nodes.add(node);
        if (leader == null || node.getIndex() > leader.getIndex()) {
            leader = node;
        }
        assert node.getWeight() == weight;
    }

    public void deleteNode(Node node) {
        nodes.remove(node);
        if (nodes.size() == 0) {
            leader = null;
        } else {
            leader = findLeader();
        }
    }

    private Node findLeader() {
        return Collections.max(nodes, Comparator.comparing(Node::getIndex));
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Node getLeader() {
        return leader;
    }

    public void setLeader(Node leader) {
        this.leader = leader;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
