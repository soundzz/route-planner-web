import java.io.FileReader;
import java.io.BufferedReader;
import java.util.*;
import java.util.Comparator;

/**
 *
 */
public class Graph {
    private int[] offsetArray;
    private float[][] nodes;
    private int[][] edges;
    private int numberOfNodes;
    private int numberOfEdges;
    private int[] costs;  // from a node to a node, costs of the edges
    private int[] parents; // parent nodes
    private int[] alreadyVisited; // nodes that have already been visited (to skip to the next node)
    private PriorityQueue<Integer> queue;
    private Comparator<Integer> nodeComparator;  // specified comparator to order the nodes in the priority queue
    private int offset; // to save the current offset
    private List<Integer> path; // to return the shortest path

    private boolean calculatedPaths;
    private int currentStartNode;


    public Graph() {

    }

    public Graph(String fileName) {
        this.readGraphData(fileName);
    }

    /**
     * reads the Graph file in the folder mapdata
     * saves the nodes, edges in the array nodes, edges
     * throws file not found exception e, if the file is not found
     *
     * @param fileName ...
     */
    public void readGraphData(String fileName) {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("mapdata/" + fileName));


            boolean dataStart = false;
            String line = null;
            while (!dataStart) {
                line = reader.readLine();
                if (!line.contains("#")) {
                    dataStart = true;
                }
            }

            numberOfNodes = Integer.parseInt(reader.readLine());
            numberOfEdges = Integer.parseInt(reader.readLine());
            nodes = new float[2][numberOfNodes];
            edges = new int[3][numberOfEdges];

            Scanner scanner = null;

            for (int i = 0; i < numberOfNodes; i++) {

                //reader.readLine();

                scanner = new Scanner(reader.readLine());


                scanner.next();
                scanner.next();

                nodes[0][i] = Float.parseFloat(scanner.next()); //Latitude
                nodes[1][i] = Float.parseFloat(scanner.next()); //Longitude

            }
            //scanner.close();
            for (int i = 0; i < numberOfEdges; i++) {
                scanner = new Scanner(reader.readLine());
                edges[0][i] = Integer.parseInt(scanner.next()); //srcID
                edges[1][i] = Integer.parseInt(scanner.next()); //trgID
                edges[2][i] = Integer.parseInt(scanner.next()); //cost
            }
            scanner.close();
            calculateOffset();


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public int nearestNeighbor(float latitude, float longitude){
        int solution = -1;
        float bestDist = Float.MAX_VALUE;
        float currentDist = 0;
        for(int i = 0; i < numberOfNodes; i++){
            currentDist = (float) (Math.pow(nodes[0][i] - latitude, 2) + Math.pow(nodes[1][i] - longitude, 2));
            if(currentDist < bestDist){
                bestDist = currentDist;
                solution = i;
            }
        }
        return solution;
    }

    /**
     * calculateOffset creates the Offset- Array
     */


    public void calculateOffset() {
        offsetArray = new int[numberOfNodes]; //all entries are 0 at beginning
        for (int i = 1; i < numberOfEdges; i++) {
            offsetArray[edges[0][0]] = 0;
            if (edges[0][i] != edges[0][i - 1]) {
                offsetArray[edges[0][i]] = i;
            }

        }

    }

    /**
     * initialize sets all costs to infinity except the starting node that has 0.
     * We set all parents to null ( null = -1)
     * We create the priority queue with a specific comparator
     *
     * @param start
     *
     */

    public void initialize(int start) {
        calculatedPaths = false;
        parents = new int[numberOfNodes];
        costs = new int[numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++) {
            parents[i] = -1; // our null, because 0 is a node index, maybe not needed.
            costs[i] = Integer.MAX_VALUE; // all other nodes get distance infinity
        }
        costs[start] = 0;
        queue = new PriorityQueue<>(numberOfNodes, Comparator.comparingInt(node -> costs[node]));
        queue.add(start);
    }


    /**
     * updateCosts updates the costs for the paths
     *
     * @param node_A
     * @param node_B
     * @param currentOffset
     */

    public void updateCosts(int node_A, int node_B, int currentOffset) {
        int newCosts;
        if (costs[node_A] == Integer.MAX_VALUE) {
            newCosts = costs[node_A];
            //System.out.println("Starting node has no outgoing edges");
        } else {
            newCosts = costs[node_A] + edges[2][currentOffset];
        }

        if (newCosts < costs[node_B]) {
            costs[node_B] = newCosts;
            parents[node_B] = node_A;
        }
    }

    /**
     * Dijkstra does the Dijkstra algorithm
     * there are some outcommented print lines for debugging
     * we chose the lazy insert method because we cant update the elements in the pq
     *
     * @param startNode
     * @param targetNode
     */

    public void Dijkstra(int startNode, int targetNode) {
        //System.out.println("dijkstra");

        // initialize  parents, costs and priority queue
        initialize(startNode);
        alreadyVisited = new int[numberOfNodes];
        while (!queue.isEmpty()) {
            int currentNode = queue.poll();  // gets node with min costs to start node and deletes currentNode
            if (currentNode == targetNode) {
                break;
            }
            if (alreadyVisited[currentNode] == 1) {
                continue;
            } else {
                alreadyVisited[currentNode] = 1;
            }
            //System.out.println(currentNode); // REIHENFOLGE
            offset = offsetArray[currentNode];
            while (offset < numberOfEdges && edges[0][offset] == currentNode) {


                updateCosts(currentNode, edges[1][offset], offset);

                queue.add(edges[1][offset]);


                offset++;
            }
        }
        /*for(int i =0; i < numberOfNodes; i++){
            System.out.println("Node: "+ i+ "  Costs: "+ costs[i]);
        }*/
        if (targetNode == -1) {
            calculatedPaths = true;
            currentStartNode = startNode;
        }

        //System.out.println("complete");
    }

    /**
     *
     *
     * @param targetNode
     * @return path
     *
     */

    public List<Integer> shortestPathTo(int targetNode) {
        path = new ArrayList<>();
        path.add(targetNode);
        int currentNode = targetNode;
        while (parents[currentNode] != -1) {
            currentNode = parents[currentNode];
            path.add(0, currentNode);
        }
        return path;
    }


    /**
     * Method for system out.
     */
    public void printNodes() {
        for (int i = 0; i < numberOfNodes; i++) {
            System.out.println("Offset: " + i + "  Eintrag " + offsetArray[i]);
        }
    }

    //Interface methods below

    /**
     * @param start  Start node
     * @param target Target node
     * @return shortest Path from start to target
     */
    public List<Integer> startToTargetPath(int start, int target) {
        this.Dijkstra(start, target);
        return this.shortestPathTo(target);
    }

    /**
     * @param start  Start node
     * @param target Target node
     * @return costs of shortest Path from start to target
     */
    public int pathCost(int start, int target) {
        this.Dijkstra(start, target);

        return costs[target];

    }

    /**
     * oneToAll does the one- to- all Dijkstra
     *
     * there are outcommented lines for debugging
     * @param startNode Start node
     * @return minimal path costs for all nodes
     */
    public int[] oneToAll(int startNode) {

        //System.out.println("dijkstra");

        // initialize  parents, costs and priority queue
        initialize(startNode);
        alreadyVisited = new int[numberOfNodes];
        while (!queue.isEmpty()) {
            int currentNode = queue.poll();  // gets node with min costs to start node and deletes currentNode
            if (alreadyVisited[currentNode] == 1) {
                continue;
            } else {
                alreadyVisited[currentNode] = 1;
            }
            //System.out.println(currentNode); // REIHENFOLGE
            offset = offsetArray[currentNode];
            while (offset < numberOfEdges && edges[0][offset] == currentNode) {


                updateCosts(currentNode, edges[1][offset], offset);

                queue.add(edges[1][offset]);
                offset++;
            }
        }
        /*for(int i =0; i < numberOfNodes; i++){
            System.out.println("Node: "+ i+ "  Costs: "+ costs[i]);
        }*/
        calculatedPaths = true;
        currentStartNode = startNode;

        //System.out.println("complete");

        return costs;
    }
    public float[][] getNodes(){
        return nodes;
    }

    /**
     *
     * @param start
     * @param target
     * @return the costs to the target
     */

    public int batchQuery(int start, int target) {
        if (!calculatedPaths || currentStartNode != start) {
            return oneToAll(start)[target];
        } else {
            return costs[target];
        }
    }

    /**
     *
     * @return Number of Nodes (int)
     */

    public int getNumberOfNodes() {
        return numberOfNodes;
    }
}
