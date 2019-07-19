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
            if (axis == 1) {
                if (splitNode.getLatitude() > currentNode.getLatitude()) {
                    leftChildren.add(currentNode);
                } else if (splitNode.getLatitude() <= currentNode.getLatitude() && currentNode != splitNode) {
                    rightChildren.add(currentNode);
                }
            } else if (axis == 0) {

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
        return NN(root, latitude, longitude, 1, Double.MAX_VALUE,root);
    }
//are left child right child = null?


    public Node NN ( Node node, double  longitude, double latitude, int depth, double distance, Node oldbest ){
        // x is the longitude, y is the latidue
        // the depth is needed for the axes to divide in x and y, the start depth is 1
        // distance is the euclidean distance initialzied with Double.MAX_ Value

        // initializing the variables
        Node currentresult = node;
        Node result =oldbest;
        Node test = null;
        int axis = depth % 2;
        double currentdist = Double.MAX_VALUE;
        double testdist;
        double resultdist= Double.MAX_VALUE;

        // check if we already have the best node

        if(node != null && result !=null){
            resultdist= Math.sqrt(Math.pow(result.getLatitude()-latitude,2)+Math.pow(result.getLongitude()-longitude,2));
            currentdist = Math.sqrt( Math.pow( currentresult.getLongitude() - longitude,2) + Math.pow(currentresult.getLatitude() - latitude ,2));
            if(resultdist < currentdist){
                currentresult =result;
            }
            if (node.getRightChild() != null){
                double distrightChild = Math.sqrt(Math.pow(node.getRightChild().getLatitude() - latitude,2) + Math.pow(node.getRightChild().getLongitude() -longitude,2));
                if(distrightChild < currentdist){
                    currentresult = NN(node.getRightChild(), longitude, latitude, depth + 1, distrightChild, node);
                }
            }
            if (node.getLeftChild() != null){
                double distleftChild = Math.sqrt(Math.pow(node.getLeftChild().getLatitude() - latitude, 2) + Math.pow(node.getLeftChild().getLongitude() - longitude, 2));
                if (distleftChild < currentdist){
                    currentresult = NN(node.getLeftChild(), longitude, latitude, depth, distleftChild, node);
                }
            }
        }





        // case 1: axis = 1, we check the longitude

        if (axis == 1 && node != null){
            currentdist = Math.sqrt( Math.pow( node.getLongitude() - longitude,2) + Math.pow(node.getLatitude() - latitude ,2));
            //System.out.println("Test root node x " + node.getNodeID() +" " +node.getLongitude() );
            if (node.getLongitude() > longitude && node.getLeftChild() != null){
                //System.out.println("Test left child, x Achse: " + node.getNodeID() +" " + longitude);
                double distleftChild = Math.sqrt(Math.pow(node.getLeftChild().getLatitude() - latitude,2) + Math.pow(node.getLeftChild().getLongitude() -longitude,2));
                currentresult = NN(node.getLeftChild(), longitude, latitude, depth + 1, currentdist, node);
                currentdist = Math.sqrt( Math.pow( currentresult.getLongitude() - longitude,2) + Math.pow(currentresult.getLatitude() - latitude ,2));


                // hypersphere to check if there is a false nearest neighbour

                if (currentdist > Math.sqrt(Math.pow(currentresult.getLatitude()-latitude,2)) && node.getRightChild() != null){
                    // geht in hs und speichtert test
                    //System.out.println("Test left child, x Achse hs und right: " + node.getNodeID());
                    test =  NN(node.getRightChild(), longitude, latitude, depth, currentdist, node);
                    testdist = Math.sqrt( Math.pow( test.getLongitude() - longitude,2) + Math.pow(test.getLatitude() - latitude ,2));
                    if (currentdist > testdist){
                        //test ist jetzt node
                        currentresult = test;
                    }
                }


            } else {
                if (node.getRightChild()!= null && node.getLongitude() < longitude) {
                    double distrightChild = Math.sqrt(Math.pow(node.getRightChild().getLatitude() - latitude,2) + Math.pow(node.getRightChild().getLongitude() -longitude,2));
                    //System.out.println("Test right child, x Achse: " + node.getNodeID());
                    currentresult = NN(node.getRightChild(), longitude, latitude, depth + 1, currentdist, node);
                    currentdist = Math.sqrt( Math.pow( currentresult.getLongitude() - longitude,2) + Math.pow(currentresult.getLatitude() - latitude ,2));

                }
                // hypersphere to check if there is a false nearest neighbour

                if (currentdist > Math.sqrt(Math.pow(currentresult.getLatitude()-latitude,2)) && node.getLeftChild() != null ){
                    test =  NN(node.getLeftChild(), longitude, latitude, depth, currentdist, node);
                    testdist =  Math.sqrt( Math.pow( test.getLongitude() - longitude,2) + Math.pow(test.getLatitude() - latitude ,2));
                    if (currentdist > testdist){
                        currentresult = test;
                    }
                }
            }

            // case 2: axis = 0, we check the latitude
            // is the point not as high in the latitude, it is a left child, if the latitude is bigger its a right child

        } else if ( axis == 0 && node != null) {
            currentdist =  Math.sqrt( Math.pow( node.getLongitude() - longitude,2) + Math.pow(node.getLatitude() - latitude ,2));
            //System.out.println("Test root node y " + node.getNodeID() +" "+latitude);
            if ( node.getLatitude() > latitude && node.getLeftChild() != null) {
                double distleftChild = Math.sqrt(Math.pow(node.getLeftChild().getLatitude() - latitude, 2) + Math.pow(node.getLeftChild().getLongitude() - longitude, 2));
                //System.out.println("Test left child, y Achse: +" + node.getNodeID());
                currentresult = NN(node.getLeftChild(), longitude, latitude, depth + 1, currentdist, node);
                currentdist = Math.sqrt(Math.pow(currentresult.getLongitude() - longitude, 2) + Math.pow(currentresult.getLatitude() - latitude, 2));
            }

            // hypersphere to check if there is a false nearest neighbour

            if (currentdist > Math.sqrt(Math.pow(currentresult.getLongitude()-longitude,2)) && node.getRightChild() != null ){
                test =  NN(node.getRightChild(), longitude, latitude, depth, currentdist, node);
                testdist = Math.sqrt( Math.pow( test.getLongitude() - longitude,2) + Math.pow(test.getLatitude() - latitude ,2));
                if (currentdist > testdist ){
                    currentresult = test;
                }
            }


        } else {
            if (node.getRightChild() != null && node.getLatitude() < latitude) {
                double distrightChild = Math.sqrt(Math.pow(node.getRightChild().getLatitude() - latitude,2) + Math.pow(node.getRightChild().getLongitude() -longitude,2));
                //System.out.println("Test right child, y Achse: " + node.getNodeID());
                currentresult = NN(node.getRightChild(), longitude, latitude, depth + 1, currentdist, node);
                currentdist = Math.sqrt( Math.pow( currentresult.getLongitude() - longitude,2) + Math.pow(currentresult.getLatitude() - latitude ,2));
            }
            // hypersphere to check if there is a false nearest neighbour

            if (currentdist > Math.sqrt(Math.pow(currentresult.getLongitude()-longitude,2)) && node.getLeftChild() != null){
                test =  NN(node.getLeftChild(), longitude, latitude, depth, currentdist, node);
                testdist = Math.sqrt( Math.pow( test.getLongitude() - longitude,2) + Math.pow(test.getLatitude() - latitude ,2));
                if (currentdist > testdist){
                    currentresult = test;
                }
            }


        }
        if ( currentresult != null && result != null ) {
            //System.out.println("Test current best1 : " + currentresult.getNodeID());

        }else if( result != null){
            //System.out.println("Test result begin1: " + result.getNodeID());
        }




        if ( currentresult != null && result != null ) {
            //System.out.println("Test current best2 : " + currentresult.getNodeID());

        }else if( result != null){
            //System.out.println("Test result begin2: " + result.getNodeID());
        }



        return currentresult;
    }




    public ArrayList<Node> getNodeList() {
        return nodeList;
    }




}
