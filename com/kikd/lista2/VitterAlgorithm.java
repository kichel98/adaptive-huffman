/*
 * author: Piotr Andrzejewski
 */
package com.kikd.lista2;

import java.util.ArrayList;
import java.util.List;

/*
    Implementation based on "Introduction to Data Compression" by Sayood
 */
public class VitterAlgorithm {
    private Tree tree;

    public VitterAlgorithm() {
        tree = new Tree();
    }

    /*
        byte (type in Java) range - -127 to 128
        symbol range - 0 to 255
     */
    public StringBuilder encode(byte[] input) {
        StringBuilder output = new StringBuilder();
        for (byte symbol : input) {
            if (tree.containsSymbol(symbol)) {
                output.append(tree.getPathCodeForSymbol(symbol));
            } else {
                output.append(tree.getPathCodeForNYT());
                output.append(generateFixedCodeForNewSymbol(symbol));
            }
            update(symbol);
        }
        return output;
    }

    public byte[] decode(String input) {
        List<Byte> bytes = new ArrayList<>();
        int currentBitIdx = 0;

        while (currentBitIdx != input.length()) { // OR -1!!!
            Object[] leafAndIndex = goToLeafFromRoot(input, currentBitIdx);
            Node node = (Node) leafAndIndex[0];
            currentBitIdx = (int) leafAndIndex[1];

            byte symbol;

            if (node == tree.getNYT()) {
                String fixedCode = input.substring(currentBitIdx, currentBitIdx + 8);
                currentBitIdx += 8;
                symbol = getSymbolFromFixedCode(fixedCode);

            } else {
                symbol = node.getSymbol();
            }
            bytes.add(symbol);
            update(symbol);
        }



        return byteListToByteArray(bytes);
    }

    private void update(byte symbol) {
        Node actualNode;
        if (tree.containsSymbol(symbol)) {
            actualNode = tree.getNodeForSymbol(symbol);
            swapAndIncrement(actualNode);

        } else {
            actualNode = tree.giveBirthFromNYT(symbol);
        }

        while (actualNode != tree.getRoot()) {
            actualNode = actualNode.getParent();
            swapAndIncrement(actualNode);
        }
    }

    private Object[] goToLeafFromRoot(String input, int currentBitIdx) {
        Node actualNode = tree.getRoot();
        while (actualNode.isInternal()) {
            char bit = input.charAt(currentBitIdx);
            if (bit == '0') {
                actualNode = actualNode.getLeft();
            } else {
                actualNode = actualNode.getRight();
            }
            currentBitIdx++;
        }
        return new Object[] {actualNode, currentBitIdx};
    }

    private void swapAndIncrement(Node actualNode) {
        Tree.BlockWithIdx nodeBlock = tree.getBlockOfWeight(actualNode.getWeight());
        Node leader = nodeBlock.block.getLeader();
        if (actualNode != leader && leader != actualNode.getParent()) {
            swapNodes(actualNode, leader, nodeBlock.block);
        }
        tree.incrementNodeWeight(actualNode);
    }

    private void swapNodes(Node node, Node leader, Block block) {
        Node nodeParent = node.getParent();
        Node leaderParent = leader.getParent();
        int nodeIndex = node.getIndex();
        int leaderIndex = leader.getIndex();
        boolean isNodeRightChild = node.isRightChild();
        boolean isLeaderRightChild = leader.isRightChild();

        if (isLeaderRightChild) {
            leaderParent.setRight(node);
        } else {
            leaderParent.setLeft(node);
        }
        node.setParent(leaderParent);

        if (isNodeRightChild) {
            nodeParent.setRight(leader);
        } else {
            nodeParent.setLeft(leader);
        }
        leader.setParent(nodeParent);

        node.setIndex(leaderIndex);
        leader.setIndex(nodeIndex);

        block.setLeader(node);

    }

    private String generateFixedCodeForNewSymbol(byte symbol) {
        int symbolAfterOffset = symbol + 128; // 0 - 255
        return String.format("%8s", Integer.toBinaryString(symbolAfterOffset))
                .replace(' ', '0');
    }

    private byte getSymbolFromFixedCode(String code) {
        int codeValue = Integer.parseInt(code, 2); // 0 - 255
        return (byte) (codeValue - 128); // -128 - 127
    }

    private byte[] byteListToByteArray(List<Byte> bytes) {
        byte[] output = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            output[i] = bytes.get(i);
        }
        return output;
    }

    public Tree getTree() {
        return tree;
    }
}
