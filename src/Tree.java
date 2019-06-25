import java.util.concurrent.ThreadLocalRandom;
import java.util.*;
public class Tree {
    private Node root;
    private ArrayList<Node> nodeList;

    public Tree(float[][] nodes){
            for(int i = 0; i < nodes[1].length; i++){
                nodeList.add(new Node(i, nodes[0][i], nodes[1][i]));
            }

            root = constructTree(nodeList, 0);
    }

    private Node constructTree(ArrayList<Node> children, int depth){
        int axis = depth % 2; //0 = y-achse (latitude), 1 = x-Achse (longitude)
        int splitIndex = ThreadLocalRandom.current().nextInt(0, children.size());
        Node splitNode = children.get(splitIndex);

        ArrayList<Node> leftChildren = null;
        ArrayList<Node> rightChildren = null;

        for(int i = 0; i < children.size(); i++){
            Node currentNode = children.get(i);
            if (axis == 0) {
                if (splitNode.getLatitude() > currentNode.getLatitude()) {
                    leftChildren.add(currentNode);
                }else if (splitNode.getLatitude() <= currentNode.getLatitude() && currentNode != splitNode){
                    rightChildren.add(currentNode);
                }
            }else if (axis == 1){

                if (splitNode.getLongitude() > currentNode.getLongitude()) {
                    leftChildren.add(currentNode);
                }else if (splitNode.getLongitude() <= currentNode.getLongitude() && currentNode != splitNode){
                    rightChildren.add(currentNode);
                }

            }
        }

        splitNode.setLeftChild(constructTree(leftChildren, depth+1));
        splitNode.setRightChild(constructTree(rightChildren, depth+1));
        return splitNode;
    }
    private Node nearestNeighbor(float latitude, float longitude){
            return nodeChecker(root, Float.MAX_VALUE, latitude, longitude, 0);
    }
    private Node nodeChecker(Node node, float bestDist, float latitude, float longitude, int depth){
        Node result = null;
        int axis = depth % 2;
        if(axis == 0){
            float dist = (float)Math.pow(node.getLatitude()-latitude, 2);
            if(dist < bestDist){
                result = node;
            }
        }else if(axis == 1){

        }



        return result;
    }
}
