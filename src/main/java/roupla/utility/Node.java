package roupla.utility;

public class Node {
    private Node leftChild;
    private Node rightChild;
    private int nodeID;
    private double latitude;
    private double longitude;

    public Node(int ID, double lat, double lon){
        nodeID = ID;
        latitude = lat;
        longitude = lon;
    }

    public double getLatitude(){
        return latitude ;
    }
    public double getLongitude(){
        return  longitude;
    }
    public void setLeftChild(Node child){
        leftChild = child;
        return;
    }
    public void setRightChild(Node child){
        rightChild = child;
        return;
    }
    public Node getLeftChild(){
        return leftChild;
    }
    public Node getRightChild(){
        return rightChild;
    }
    public Double getDist(Node node){
        return Math.sqrt(Math.pow(this.latitude - node.latitude, 2) + Math.pow(this.longitude - node.longitude, 2));
    }
    public int getNodeID(){
        return nodeID;
    }
}
