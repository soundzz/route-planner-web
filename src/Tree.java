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

    private Node nodeChecker(Node node, float bestDist, float targetLatitude, float targetLongitude, int depth) {
        Node result = null;
        int axis = depth % 2;
        if (axis == 0) {
            if (node.getLatitude() > targetLatitude) {
                result = nodeChecker(node.getLeftChild(), bestDist, targetLatitude, targetLongitude, depth + 1);
            } else if (node.getLatitude() <= targetLatitude) {
                result = nodeChecker(node.getRightChild(), bestDist, targetLatitude, targetLongitude, depth + 1);
            }
        } else if (axis == 1) {
            if (node.getLongitude() > targetLongitude) {
                result = nodeChecker(node.getLeftChild(), bestDist, targetLatitude, targetLongitude, depth + 1);
            } else if (node.getLongitude() <= targetLongitude) {
                result = nodeChecker(node.getRightChild(), bestDist, targetLatitude, targetLongitude, depth + 1);
            }
        }
        float dist = node.getDist(result);
        if (dist < bestDist) {
            bestDist = dist;
        } else if (dist >= bestDist) {
            return node;
        }
        return result;
    }
    public ArrayList<Node> getNodeList(){
        return nodeList;
    }
}
