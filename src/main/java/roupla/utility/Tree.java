package roupla.utility;

import java.util.concurrent.ThreadLocalRandom;
import java.util.*;

public class Tree {
    private Node root;
    private ArrayList<Node> nodeList = new ArrayList<Node>();

    public Tree(double[][] nodes) {
        for (int i = 0; i < nodes[1].length; i++) {
            nodeList.add(new Node(i, nodes[0][i], nodes[1][i]));
        }

        root = constructTree(nodeList, true);
    }

    private Node constructTree(ArrayList<Node> children, boolean depth) {

        int splitIndex;
        if (children == null || children.size() == 0) {
            return null;
        }
        if (children.size() == 1) {
            splitIndex = 0;
        } else {
            splitIndex = ThreadLocalRandom.current().nextInt(children.size());
        }
        Node splitNode = children.get(splitIndex);

        ArrayList<Node> leftChildren = new ArrayList<Node>();
        ArrayList<Node> rightChildren = new ArrayList<Node>();

        for (int i = 0; i < children.size(); i++) {
            Node currentNode = children.get(i);
            if (depth) { //lat
                if (splitNode.getLatitude() > currentNode.getLatitude()) {
                    leftChildren.add(currentNode);
                } else if (splitNode.getLatitude() <= currentNode.getLatitude() && currentNode != splitNode) {
                    rightChildren.add(currentNode);
                }
            } else { // (!depth)

                if (splitNode.getLongitude() > currentNode.getLongitude()) {
                    leftChildren.add(currentNode);
                } else if (splitNode.getLongitude() <= currentNode.getLongitude() && currentNode != splitNode) {
                    rightChildren.add(currentNode);
                }

            }
        }

        splitNode.setLeftChild(constructTree(leftChildren, !depth));
        splitNode.setRightChild(constructTree(rightChildren, !depth));
        return splitNode;
    }

    public double calcDist(Node n, double latitude, double longitude){
        return Math.pow(n.getLatitude() - latitude, 2) + Math.pow(n.getLongitude() - longitude, 2);
    }
    public Node nearestNeighbor(double latitude, double longitude) {
        return nearestNeighbor(root, latitude, longitude, true, Double.MAX_VALUE, null);
    }


    public Node nearestNeighbor(Node node, double latitude, double longitude, boolean depth, double currentbestdist, Node currentbestnode) {

        /**
         *
         * @param node, latitude, longitude, depth, currentbestdist
         *
         */

        //initializing variables
        double distance = Double.MAX_VALUE;
        if(node == null){
            return currentbestnode;
        }
        if (node != null) {
            distance = Math.pow(node.getLatitude() - latitude, 2) + Math.pow(node.getLongitude() - longitude, 2);
        }

        //check if the current node is best

        if (distance < currentbestdist) {
            currentbestnode = node;
            currentbestdist = distance;
        }

        if (depth) { //lat
            if (node.getLatitude() < latitude) {
                if (node.getRightChild() != null) {
                    currentbestnode = nearestNeighbor(node.getRightChild(), latitude, longitude, false, currentbestdist, currentbestnode);
                    currentbestdist = calcDist(currentbestnode, latitude, longitude);

                }
                if (node.getLeftChild() != null && currentbestdist > Math.pow(node.getLatitude() - latitude, 2)) {
                    currentbestnode = nearestNeighbor(node.getLeftChild(), latitude, longitude, false, currentbestdist, currentbestnode);

                }


            } else { // node.getLatitude > latitude
                if (node.getLeftChild() != null) {
                    currentbestnode = nearestNeighbor(node.getLeftChild(), latitude, longitude, false, currentbestdist, currentbestnode);
                    currentbestdist = calcDist(currentbestnode, latitude, longitude);

                }
                if (node.getRightChild() != null && currentbestdist > Math.pow(node.getLatitude() - latitude, 2)) {
                    currentbestnode = nearestNeighbor(node.getRightChild(), latitude, longitude, false, currentbestdist, currentbestnode);

                }
            }
        } else { // case axis == 1 // long
            if (node.getLongitude() < longitude) {
                if (node.getRightChild() != null) {
                    currentbestnode = nearestNeighbor(node.getRightChild(), latitude, longitude, true, currentbestdist, currentbestnode);
                    currentbestdist = calcDist(currentbestnode, latitude, longitude);

                }
                if (node.getLeftChild() != null && currentbestdist > Math.pow(node.getLongitude() - longitude, 2)) {
                    currentbestnode = nearestNeighbor(node.getLeftChild(), latitude, longitude, true, currentbestdist, currentbestnode);

                }

            } else { //node.getLongitude > longitude
                if (node.getLeftChild() != null) {
                    currentbestnode = nearestNeighbor(node.getLeftChild(), latitude, longitude, true, currentbestdist, currentbestnode);
                    currentbestdist = calcDist(currentbestnode, latitude, longitude);

                }
                if (node.getRightChild() != null && currentbestdist > Math.pow(node.getLongitude() - longitude, 2)) {
                    currentbestnode = nearestNeighbor(node.getRightChild(), latitude, longitude, true, currentbestdist, currentbestnode);

                }
            }
        }

        return currentbestnode;
    }


    public ArrayList<Node> getNodeList() {
        return nodeList;
    }


}
