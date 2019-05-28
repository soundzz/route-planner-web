import java.io.FileReader;
import java.io.BufferedReader;
import java.util.*;
import java.util.Comparator;

/**
 * the class Graph reads the File with the graph information
 * the array Nodes contains the nodes with length and width.
 * the array Edges contains the edges with starting node, target node and costs.
 * OffsetArray contains the offset, the first place of the edge in the array edges of a node.
 * numberOfNodes is the number of nodes in the graph.
 * numberOfEdges is the number of edges in the graph.
 * the arrays distance and predecessor are the information needed for the Dijkstra algorithm.
 */
public class Graph {
    private int[] offsetArray;
    private float[][] nodes;
    private int[][] edges;   // war mal float
    private int numberOfNodes;
    private int numberOfEdges;
    private int[] costs;  //int for now
    private int[] parents;
    private PriorityQueue<Integer> queue;
    private Comparator<Integer> nodeComparator;
    private int offset;
    private List<Integer> path;

    private boolean calculatedPaths;
    private int currentStartNode;


    public Graph() {

    }

    public Graph(String fileName) {
        this.readGraphData(fileName);
    }

    /**
     * readGraphData reads the file and saves the information in the arrays Nodes and Edges.
     * the methodes works in the following way:
     * 1) BufferedReader reads the file.
     * 2) the first lines of the file gets skipped
     * 3) a scanner gets created and goes through the lines
     * <p>
     * If no file is found the programm throws an exception.
     *
     * @param fileName ...
     */
    public void readGraphData(String fileName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));


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
            nodes = new float[numberOfNodes][2];
            edges = new int[numberOfEdges][3];

            Scanner scanner = null;

            for (int i = 0; i < numberOfNodes; i++) {
                scanner = new Scanner(reader.readLine());

                scanner.next();
                scanner.next();

                nodes[i][0] = Float.parseFloat(scanner.next()); //Latitude
                nodes[i][1] = Float.parseFloat(scanner.next()); //Longitude
            }
            scanner.close();
            for (int i = 0; i < numberOfEdges; i++) {
                scanner = new Scanner(reader.readLine());
                edges[i][0] = Integer.parseInt(scanner.next()); //srcID
                edges[i][1] = Integer.parseInt(scanner.next()); //trgID
                edges[i][2] = Integer.parseInt(scanner.next()); //cost
            }
            scanner.close();
            calculateOffset();


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * calculateOffset creates the Offset- Array
     * we dont have the offset of a node that has no outgoing edges.
     * we cant compare offsets with each other
     */


    public void calculateOffset() {
        offsetArray = new int[numberOfNodes]; //all entries are 0 at beginning
        for (int i = 1; i < numberOfEdges; i++) {
            offsetArray[edges[0][0]] = 0;
            if (edges[i][0] != edges[i - 1][0]) {
                offsetArray[edges[i][0]] = i;
            }

        }

    }

    /**
     * initialize sets all costs to infinity except the starting node that has 0.
     * We set all parents to null, maybe its not needed.
     */

    public void initialize(int start, Comparator<Integer> comp) {
        parents = new int[numberOfNodes];
        costs = new int[numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++) {
            parents[i] = -1; // our null, because 0 is a node index, maybe not needed.
            costs[i] = Integer.MAX_VALUE; // all other nodes get distance infinity
        }
        costs[start] = 0;
        queue = new PriorityQueue<>(numberOfNodes, comp);
        for (int node = 0; node < numberOfNodes; node++) {
            queue.add(node);
        }
    }


    /**
     * Dijkstra does the Dijkstra algorithm.
     * First we overrite the comparator of the priority queue.
     */

    public void updateCosts(int node_A, int node_B, int currentOffset) {
        int newCosts = costs[node_A] + edges[currentOffset][2];
        if (newCosts < costs[node_B]) {
            costs[node_B] = newCosts;
            parents[node_B] = node_A;
        }
    }

    public void Dijkstra(int startNode) {
        System.out.println("dijkstra");
        //Comparator changes the order of the priority queue sorted by the distance to the starting node
        Comparator<Integer> nodeComparator = (Integer node_A, Integer node_B) -> costs[node_A] - costs[node_B];

        ;
        // initialize  parents, costs and priority queue

        initialize(startNode, nodeComparator);

        while (!queue.isEmpty()) {
            int currentNode = queue.poll();  // gets node with min costs to start node and deletes currentNode
            //System.out.println(currentNode); // REIHENFOLGE
            offset = offsetArray[currentNode];
            while (edges[offset][0] == currentNode) {
                if (queue.contains(edges[offset][1])) {  // if neighbour is in queue
                    updateCosts(currentNode, edges[offset][1], offset);
                }
                offset++;
                if (offset >= numberOfEdges) {
                    break;
                }
            }
        }
        /*for(int i =0; i < numberOfNodes; i++){
            System.out.println("Node: "+ i+ "  Costs: "+ costs[i]);
        }*/
        calculatedPaths = true;
        currentStartNode = startNode;
        System.out.println("complete");
    }

    public List<Integer> shortestPathTo(int targetNode) {
        path = new ArrayList<Integer>();
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
            // System.out.println("Node ID: " + i + " | x: " + Nodes[i][0] + " | y: " + Nodes[i][1]);
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
        this.Dijkstra(start);
        return this.shortestPathTo(target);
    }

    /**
     * @param start  Start node
     * @param target Target node
     * @return costs of shortest Path from start to target
     */
    public int pathCost(int start, int target) {
        if(!calculatedPaths || !(currentStartNode == start)){
            this.Dijkstra(start);
        }
        return costs[target];

    }

    /**
     * @param start Start node
     * @return minimal path costs for all nodes
     */
    public int[] oneToAll(int start) {
        this.Dijkstra(start);
        return costs;
    }
}
