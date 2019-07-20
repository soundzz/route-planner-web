import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

public class Hub {
    private List<Integer> query = new ArrayList<>(); //in pairs of start/target nodes at even/odd indices
    private int[] results;

    /**
     * query reads the query file and calculates the minimal costs
     *
     * @param fileName
     * @param graph
     * @param batch
     */

    public void query(String fileName, Graph graph, boolean batch) {

        //read query from file:
        BufferedReader reader;
        try {

            reader = new BufferedReader(new FileReader("Benchs/" + fileName));
            Scanner scanner;
            while (reader.ready()) {
                scanner = new Scanner(reader.readLine());
                while (scanner.hasNext()) {
                    query.add(Integer.parseInt(scanner.next()));
                }
                scanner.close();
            }
        } catch (Exception e) {
            System.out.println(e.getClass());

        }

        //calculate minimal path costs

        results = new int[query.size() / 2];
        for (int i = 0; i < results.length; i++) {
            if (batch) {
                results[i] = graph.batchQuery(query.get(2 * i), query.get(2 * i + 1));
            } else {
                results[i] = graph.pathCost(query.get(2 * i), query.get(2 * i + 1));
            }

            //System.out.println("From " + query.get(2*i) + " to " + query.get(2*i+1) + ": " + results[i]);
        }
        try {
            PrintWriter solFile = new PrintWriter("output.sol");
            for (int i = 0; i < results.length; i++) {
                solFile.println(results[i]);
            }
            solFile.close();
        } catch (Exception e) {
            System.out.println(e.getClass());
        }

    }

    /**
     * the main initializes most of the instances: file, graph, hub
     * it also prints the menu
     * some outcommmeted code is from the old version
     *
     * @param args
     */
    public static void main(String args[]) {
        Graph graph = new Graph("kd-demo.fmi");
        Tree KDTree = new Tree(graph.getNodes());
        ArrayList<Node> nodes = KDTree.getNodeList();
        Iterator<Node> it = nodes.iterator();
        while (it.hasNext()) {
            Node node = it.next();
            if (node.getRightChild() == null || node.getLeftChild() == null) {
                if(node.getRightChild() == null && node.getLeftChild() != null){
                    System.out.println("NodeID: " + node.getNodeID() + ", leftChild: " + node.getLeftChild().getNodeID());

                }else if(node.getLeftChild() == null && node.getRightChild() != null){
                    System.out.println("NodeID: " + node.getNodeID() + ", rightChild: " + node.getRightChild().getNodeID());

                }
            } else {
                System.out.println("NodeID: " + node.getNodeID() + ", leftChild: " + node.getLeftChild().getNodeID() + ", rightChild: " + node.getRightChild().getNodeID());
            }
        }
        Double x_coord =  Math.random() * 10;
        Double y_coord =  Math.random() * 10;

        System.out.println("Nearest neighbor of " + x_coord + ", " + y_coord);
        Node neighbor = KDTree.nearestNeighbor(x_coord, y_coord);
        System.out.println("Solution nearestNeighbor: " + neighbor.getLatitude() + " | " + neighbor.getLongitude());

        int benchmark = graph.nearestNeighbor(x_coord, y_coord);
        System.out.println("Solution: " + graph.getNodes()[0][benchmark] + " | " + graph.getNodes()[1][benchmark]);


        /*Node result = KDTree.nearestNeighbor(53.82233040000000556f, 10.72339740000000140f);
        System.out.println(result.getLongitude() + " " + result.getLatitude());
        */
        /*

        long time;
        System.out.println("|Enter mapdata filename (must be in /mapdata):");
        Scanner input = new Scanner(System.in);
        String file = input.next();
        time = java.lang.System.currentTimeMillis();
        Graph graph = new Graph(file);
        time = java.lang.System.currentTimeMillis() - time;
        System.out.println("|Elapsed time (Data import): " + time + "ms");
        Hub hub = new Hub();
        while (true) {

            System.out.println("|Select action:");
            System.out.println("| 1 : Query file with lots of different start nodes");
            System.out.println("| 2 : Query file with request batches starting at the same node");
            System.out.println("| 3 : One-to-all Dijkstra with subsequent query");
            System.out.println("| 4 : Exit program");
            input = new Scanner(System.in);
            int action = input.nextInt();
            String queryFile;

            switch (action) {
                case 1: //batch query with lots of different start nodes
                    System.out.println("|Enter query filename (must be in /Benchs):");
                    input = new Scanner(System.in);
                    queryFile = input.next();
                    hub.query(queryFile, graph, false);
                    System.out.println("|Query processed, results can be found in output.sol");
                    break;
                case 2:
                    System.out.println("|Enter query filename (must be in /Benchs):");
                    input = new Scanner(System.in);
                    queryFile = input.next();
                    hub.query(queryFile, graph, true);
                    System.out.println("|Query processed, results can be found in output.sol");
                    break;
                case 3:
                    System.out.println("|Enter start node (0 - " + (graph.getNumberOfNodes()-1) + "):");
                    input = new Scanner(System.in);
                    int startNode = input.nextInt();
                    time = java.lang.System.currentTimeMillis();
                    int costs[] = graph.oneToAll(startNode);
                    time = java.lang.System.currentTimeMillis() - time;
                    System.out.println("|Elapsed time (one-to-all Dijkstra): " + time + "ms");
                    while(true) {


                        System.out.println("|Query target node (-1 to return to main menu): ");
                        input = new Scanner(System.in);
                        int targetNode = input.nextInt();
                        if (targetNode == -1){
                            break;
                        }
                        System.out.println("|Path costs from " + startNode + " to " + targetNode + ": " + costs[targetNode]);
                    }
                    break;
                case 4:
                    return;
                default:
                    System.out.println(">> invalid input, try again");
            }
        }
        */
        /*Graph graph = new Graph(args[1]);
        Hub hub = new Hub();
        switch(args[0]){
            case "0": //batch query with lots of different start nodes
                hub.query(args[2], graph, false);
                break;
            case "1": //batch query with lots of requests with same start node
                hub.query(args[2], graph, true);
            case "2": //start-target
                System.out.println("Path costs: " + graph.pathCost(Integer.parseInt(args[2]), Integer.parseInt(args[3])));
                break;
            case "3": // one-to-all
                int[] costs = graph.oneToAll(Integer.parseInt(args[2]));
                while(true){
                    System.out.print("Enter a target node: ");
                    Scanner input = new Scanner(System.in);
                    int target = input.nextInt();
                    System.out.println("Path costs: " + costs[target]);
                    break;

                }
            default:
                System.out.println("Invalid arguments");
        }*/

    }
}

