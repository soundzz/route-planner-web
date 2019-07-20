import java.util.concurrent.ThreadLocalRandom;
import java.util.*;

public class Tree {
    private Node root;
    private ArrayList<Node> nodeList = new ArrayList<Node>();

    public Tree(double[][] nodes) {
        for (int i = 0; i < nodes[1].length; i++) {
            nodeList.add(new Node(i, nodes[0][i], nodes[1][i]));
        }

        root = constructTree(nodeList, 0);
    }

    private Node constructTree(ArrayList<Node> children, int depth) {
        int axis = depth % 2; //0 = y-achse (latitude), 1 = x-Achse (longitude)
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
            if (axis == 0) {
                if (splitNode.getLatitude() > currentNode.getLatitude()) {
                    leftChildren.add(currentNode);
                } else if (splitNode.getLatitude() <= currentNode.getLatitude() && currentNode != splitNode) {
                    rightChildren.add(currentNode);
                }
            } else if (axis == 1) {

                if (splitNode.getLongitude() > currentNode.getLongitude()) {
                    leftChildren.add(currentNode);
                } else if (splitNode.getLongitude() <= currentNode.getLongitude() && currentNode != splitNode) {
                    rightChildren.add(currentNode);
                }

            }
        }

        splitNode.setLeftChild(constructTree(leftChildren, depth + 1));
        splitNode.setRightChild(constructTree(rightChildren, depth + 1));
        return splitNode;
    }

    public Node nearestNeighbor(double latitude, double longitude) {
        return nearestNeighbor(root, latitude, longitude, 0, Double.MAX_VALUE);
    }



    public Node nearestNeighbor(Node node, double  latitude, double longitude, int depth , double currentbestdist){

        /**
         *
         * @param node, latitude, longitude, depth, currentbestdist
         *
         */

        //initializing variables

        Node currentbestnode =null;
        Node testright= null;
        Node testleft = null;
        double distright = Double.MAX_VALUE;
        double distleft = Double.MAX_VALUE;
        double distance= Double.MAX_VALUE;
        int axis = depth % 2;

        if (node != null){
            distance = Math.sqrt( Math.pow(node.getLatitude()-latitude,2)  + Math.pow(node.getLongitude()-longitude,2) );
        }

        //check if the current node is best

        if (distance < currentbestdist){
            currentbestnode = node;
            currentbestdist = distance;
        }

        if(axis == 0){ //lat
            if(node.getLatitude() < latitude){
                if(node.getRightChild() !=null){
                    testright = nearestNeighbor(node.getRightChild(), latitude, longitude, depth +1, currentbestdist);
                }
                if (node.getLeftChild() != null && currentbestdist > Math.abs(node.getLatitude() - latitude)){
                    testleft = nearestNeighbor(node.getLeftChild(), latitude, longitude, depth +1, currentbestdist);
                }

            } else { // node.getLatitude > latitude
                if (node.getLeftChild() != null){
                    testleft = nearestNeighbor(node.getLeftChild(), latitude, longitude, depth +1, currentbestdist);
                }
                if(node.getRightChild() !=null && currentbestdist > Math.abs(node.getLatitude() - latitude)){
                    testright = nearestNeighbor(node.getRightChild(), latitude, longitude, depth +1, currentbestdist);
                }
            }
        } else { // case axis == 1 // long
            if(node.getLongitude() < longitude){
                if(node.getRightChild() !=null){
                    testright = nearestNeighbor(node.getRightChild(), latitude, longitude, depth +1, currentbestdist);
                }
                if (node.getLeftChild() != null && currentbestdist > Math.abs(node.getLongitude() - longitude)){
                    testleft = nearestNeighbor(node.getLeftChild(), latitude, longitude, depth +1, currentbestdist);
                }

            } else { //node.getLongitude > longitude
                if (node.getLeftChild() != null){
                    testleft = nearestNeighbor(node.getLeftChild(), latitude, longitude, depth +1, currentbestdist);
                }
                if(node.getRightChild() !=null && currentbestdist > Math.abs(node.getLongitude() - longitude)){
                    testright = nearestNeighbor(node.getRightChild(), latitude, longitude, depth +1, currentbestdist);
                }
            }
        }

        //set the distance

        if (testleft != null){
            distleft = Math.sqrt( Math.pow(testleft.getLatitude()-latitude,2)  + Math.pow(testleft.getLongitude()-longitude,2) );
        }
        if(testright != null){
            distright = Math.sqrt( Math.pow(testright.getLatitude()-latitude,2)  + Math.pow(testright.getLongitude()-longitude,2) );
        }

        // check if the left/right distance is better

        if (distleft < currentbestdist){
            currentbestdist = distleft;
            currentbestnode = testleft;
        }
        if (distright < currentbestdist){
            currentbestdist = distright;
            currentbestnode = testright;
        }

        return currentbestnode;
    }




    public ArrayList<Node> getNodeList() {
        return nodeList;
    }




}
