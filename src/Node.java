public class Node {
    private Node leftChild;
    private Node rightChild;
    private int nodeID;
    private float latitude;
    private float longitude;

    public Node(int ID, float lat, float lon){
        nodeID = ID;
        latitude = lat;
        longitude = lon;
    }

    public float getLatitude(){
        return latitude;
    }
    public float getLongitude(){
        return longitude;
    }
    public void setLeftChild(Node child){
        leftChild = child;
        return;
    }
    public void setRightChild(Node child){
        rightChild = child;
        return;
    }
}
