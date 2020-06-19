/*
 * author: Piotr Andrzejewski
 */
package com.kikd.lista2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Tree {

    static class BlockWithIdx {

        public Block block;

        public int index;
        public BlockWithIdx(Block block, int index) {
            this.block = block;
            this.index = index;
        }
    }
    private Node root;

    private Node NYT;
    private Map<Byte, Node> symbolMapping;
    private List<Block> blocks;
    public Tree() {
        root = new Node(255); // 255 - highest number in alphabet
        NYT = root;
        symbolMapping = new HashMap<Byte, Node>();
        Block block = new Block(root);
        blocks = new LinkedList<>(List.of(block));
    }

    public Node giveBirthFromNYT(byte symbol) {
        int indexNYT = NYT.getIndex();
        Node left = new Node(NYT, indexNYT - 2);
        Node right = new Node(NYT, indexNYT - 1, symbol, symbolMapping);
        NYT.setLeft(left);
        NYT.setRight(right);
        Node oldNYT = NYT;
        NYT = left;
        right.incrementWeight();
        oldNYT.incrementWeight();

        Block zeros = getBlockOfWeight(0).block;
        zeros.deleteNode(oldNYT);
        zeros.addNode(NYT);
        BlockWithIdx ones = getBlockOfWeight(1);
        if (ones.block == null) {
            Block newBlock = new Block();
            newBlock.setWeight(1);
            ones.block = newBlock;
            blocks.add(ones.index, newBlock);
        }
        ones.block.addNode(oldNYT);
        ones.block.addNode(right);
        return oldNYT;

    }
    public BlockWithIdx getBlockOfWeight(int weight) {
        int idx = 0;
        for (Block block : blocks) {
            if (block.getWeight() == weight) {
                return new BlockWithIdx(block, idx);
            }
            if (block.getWeight() > weight) {
                return new BlockWithIdx(null, idx);
            }
            idx++;
        }
        return new BlockWithIdx(null, blocks.size());
    }

    public void incrementNodeWeight(Node node) {
        BlockWithIdx nodeBlock = getBlockOfWeight(node.getWeight());
        int nextBlockIndex = nodeBlock.index + 1;
        nodeBlock.block.deleteNode(node);
        if (nodeBlock.block.getNodes().isEmpty()) {
            blocks.remove(nodeBlock.block);
            nextBlockIndex--;
        }
        Block nextBlock = null;
        node.incrementWeight();
        if (nextBlockIndex < blocks.size()) {
            nextBlock = blocks.get(nextBlockIndex);
        }
        if (nextBlock != null && nextBlock.getWeight() == node.getWeight()) {
            nextBlock.addNode(node);
        } else {
            Block newBlock = new Block(node);
            blocks.add(nextBlockIndex, newBlock);
        }

    }

    public boolean containsSymbol(byte symbol) {
        return symbolMapping.containsKey(symbol);
    }

    public Node getNodeForSymbol(byte symbol) {
        return symbolMapping.get(symbol);
    }

    public String getPathCodeForSymbol(byte symbol) {
        return getPathCodeForNode(symbolMapping.get(symbol));
    }

    public String getPathCodeForNYT() {
        return getPathCodeForNode(NYT);
    }

    public Node getNYT() {
        return NYT;
    }

    public Node getRoot() {
        return root;
    }

    public void setNYT(Node NYT) {
        this.NYT = NYT;
    }

    public boolean isNYT(Node node) {
        return node == NYT;
    }

    private String getPathCodeForNode(Node node) {
        StringBuilder path = new StringBuilder();
        while (node != root) {
            if (node.isRightChild()) {
                path.append(1);
            } else {
                path.append(0);
            }
            node = node.getParent();
        }
        return path.reverse().toString();
    }
}
