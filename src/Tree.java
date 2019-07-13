import java.util.concurrent.ThreadLocalRandom;
import java.util.*;

public class Tree {
    private Node root;
    private ArrayList<Node> nodeList = new ArrayList<Node>();

    public Tree(float[][] nodes) {
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

    public Node nearestNeighbor(float latitude, float longitude) {
        return nodeChecker(root, Float.MAX_VALUE, latitude, longitude, 0);
    }
//are left child right child = null?


    private Node nodeChecker(Node node, float bestDist, float latitude, float longitude, int depth) {
        Node result = null;
        float dist = 0;
        int axis = depth % 2;
        if (axis == 0 && node != null) {
            dist = (float) Math.pow(node.getLatitude() - latitude, 2);
            if (dist < bestDist) {
                bestDist = dist;
                result = node;

                float nextDistLeft;
                float nextDistRight;
                float center_y = 0;
                if (node.getLeftChild() == null) {
                    nextDistLeft = Float.MAX_VALUE;
                } else {
                    nextDistLeft = (float) Math.pow(node.getLeftChild().getLatitude() - latitude, 2);
                }
                if (node.getRightChild() == null) {
                    nextDistRight = Float.MAX_VALUE;
                } else {
                    nextDistRight = (float) Math.pow(node.getRightChild().getLatitude() - latitude, 2);
                }

                center_y = result.getLatitude();

                if (nextDistLeft < bestDist && nextDistLeft < nextDistRight) {
                    bestDist = nextDistLeft;
                    result = node.getLeftChild();
                    nodeChecker(node.getLeftChild(), Float.MAX_VALUE, latitude, longitude, 0);
                    //Circle around the current result to check if a point on the other side of the split is closer
                    if (bestDist > Math.pow(center_y - node.getLatitude(), 2)) {
                        nodeChecker(node.getRightChild(), Float.MAX_VALUE, latitude, longitude, 0);
                    }
                } else if (nextDistRight < bestDist) {
                    bestDist = nextDistRight;
                    result = node.getRightChild();
                    nodeChecker(node.getRightChild(), Float.MAX_VALUE, latitude, longitude, 0);
                    //Circle around the current result to check if a point on the other side of the split is closer
                    if (bestDist > Math.pow(center_y - node.getLatitude(), 2)) {
                        nodeChecker(node.getLeftChild(), Float.MAX_VALUE, latitude, longitude, 0);
                    }
                }
            }
        } else if (axis == 1 && node != null) {
            dist = (float) Math.pow(node.getLongitude() - longitude, 2);
            if (dist < bestDist) {
                bestDist = dist;
                result = node;
                float nextDistLeft;
                float nextDistRight;
                float center_x = 0;
                if(node.getLeftChild() == null){
                    nextDistLeft = Float.MAX_VALUE;
                }else{
                    nextDistLeft = (float) Math.pow(node.getLeftChild().getLongitude() - longitude, 2);
                }
                if(node.getRightChild() == null){
                    nextDistRight = Float.MAX_VALUE;
                }else{
                    nextDistRight = (float) Math.pow(node.getRightChild().getLongitude() - longitude, 2);
                }

                    center_x = result.getLongitude();



                if (nextDistLeft < bestDist && nextDistLeft < nextDistRight) {
                    bestDist = nextDistLeft;
                    result = node.getLeftChild();
                    nodeChecker(node.getLeftChild(), Float.MAX_VALUE, latitude, longitude, 0);
                    //Circle around the current result to check if a point on the other side of the split is closer
                    if (bestDist > Math.pow(center_x - node.getLongitude(), 2)) {
                        nodeChecker(node.getRightChild(), Float.MAX_VALUE, latitude, longitude, 0);
                    }
                } else if (nextDistRight < bestDist) {
                    bestDist = nextDistRight;
                    result = node.getRightChild();
                    nodeChecker(node.getRightChild(), Float.MAX_VALUE, latitude, longitude, 0);
                    //Circle around the current result to check if a point on the other side of the split is closer
                    if (bestDist > Math.pow(center_x - node.getLongitude(), 2)) {
                        nodeChecker(node.getLeftChild(), Float.MAX_VALUE, latitude, longitude, 0);
                    }
                }

            }
        }

        return result;
    }

    public ArrayList<Node> getNodeList() {
        return nodeList;
    }


    //__________________________________ohne hypersphere__________________________________
    /**
     private Node nodeChecker(Node node, float bestDist, float latitude, float longitude, int depth){
     Node result = null;
     int axis = depth % 2;
     if(axis == 0){
     float dist = (float)Math.pow(node.getLatitude()-latitude, 2);
     if(dist < bestDist){
     bestDist = dist;
     result = node;
     float nextDistLeft =(float) Math.pow(node.getLeftChild().getLatitude() - latitude,2);
     float nextDistRight =(float) Math.pow(node.getRightChild().getLatitude() - latitude,2);
     if (nextDistLeft < bestDist && nextDistLeft < nextDistRight){
     nodeChecker(node.getLeftChild(), Float.MAX_VALUE, latitude, longitude, 0);
     }else if(nextDistRight < bestDist){
     nodeChecker(node.getLeftChild(), Float.MAX_VALUE, latitude, longitude, 0);
     }
     }
     }else if(axis == 1){
     float dist = (float) Math.pow(node.getLongitude()- longitude, 2);
     if (dist < bestDist){
     bestDist =dist;
     result = node;
     float nextDistLeft =(float) Math.pow(node.getLeftChild().getLongitude() - longitude,2);
     float nextDistRight =(float) Math.pow(node.getRightChild().getLongitude() - longitude,2);
     if (nextDistLeft < bestDist && nextDistLeft < nextDistRight){
     nodeChecker(node.getLeftChild(), Float.MAX_VALUE, latitude, longitude, 0);
     }else if(nextDistRight < bestDist){
     nodeChecker(node.getLeftChild(), Float.MAX_VALUE, latitude, longitude, 0);
     }

     }
     }



     return result;
     }
     */
    //------------------------------------------------------------


}
