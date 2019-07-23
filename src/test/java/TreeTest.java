import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.Assert.*;

public class TreeTest {
    @Test
    public void testNearestNeighbor(){
        double min_x = Double.MAX_VALUE;
        double min_y = Double.MAX_VALUE;
        double max_x = 0;
        double max_y = 0;
        long time = System.currentTimeMillis();
        long elapsed;
        Graph graph = new Graph("germany.fmi");
        System.out.println("Created Graph in " + (System.currentTimeMillis() - time) + "ms");
        time = System.currentTimeMillis();
        Tree KDTree = new Tree(graph.getNodes());
        System.out.println("Created 2-d-Tree in " + (System.currentTimeMillis() - time) + "ms");
        ArrayList<Node> nodes = KDTree.getNodeList();
        //get minimal bounding box
        Iterator<Node> it = nodes.iterator();
        while(it.hasNext()) {
            Node n = it.next();
            if (n.getLatitude() > max_x) {
                max_x = n.getLatitude();
            }
            if(n.getLatitude() < min_x){
                min_x = n.getLatitude();
            }
            if(n.getLongitude() > max_y){
                max_y = n.getLongitude();
            }
            if(n.getLongitude() < min_y){
                min_y = n.getLongitude();
            }
        }
        //create array with test points
        int numberOfSamples = 100;
        double TestArray[][] = new double[2][numberOfSamples];
        for(int i = 0; i < numberOfSamples; i++){
            TestArray[0][i] = min_x + Math.random() * (max_x - min_x);
            TestArray[1][i] = min_y + Math.random() * (max_y - min_y);
        }
        //Get results with simple iteration:
        double simpleSolution[][] = new double[2][numberOfSamples];
        double[][] nodeArray = graph.getNodes();
        time = System.currentTimeMillis();
        for(int i = 0; i < numberOfSamples; i++){
            int sol = graph.nearestNeighbor(TestArray[0][i], TestArray[1][i]);
            simpleSolution[0][i] = nodeArray[0][sol];
            simpleSolution[1][i] = nodeArray[1][sol];
        }
        elapsed = System.currentTimeMillis() - time;
        System.out.println("Finished simple iteration in " + elapsed + "ms");
        System.out.println("Average Time per point: " + ((double)elapsed / numberOfSamples) + "ms");


        //Get results with kd-tree:
        double treeSolution[][] = new double[2][numberOfSamples];
        time = System.currentTimeMillis();
        for(int i = 0; i < numberOfSamples; i++){
            Node sol_node = KDTree.nearestNeighbor(TestArray[0][i], TestArray[1][i]);
            treeSolution[0][i] = sol_node.getLatitude();
            treeSolution[1][i] = sol_node.getLongitude();
        }

        elapsed = System.currentTimeMillis() - time;
        System.out.println("Finished k-d-Tree nearestNeighbor search in " + elapsed + "ms");
        System.out.println("Average Time per point: " + ((double)elapsed / numberOfSamples) + "ms");
        assertArrayEquals(treeSolution, simpleSolution);
    }
}
