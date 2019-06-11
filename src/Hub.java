import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;


public class Hub {
    private List<Integer> query = new ArrayList<>(); //in pairs of start/target nodes at even/odd indices
    private int[] results;

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

        results = new int[query.size()/2];
        for (int i = 0; i < results.length; i++){
            if(batch){
                results[i] = graph.batchQuery(query.get(2*i), query.get(2*i+1));
            }else{
                results[i] = graph.pathCost(query.get(2*i), query.get(2*i+1));
            }

            //System.out.println("From " + query.get(2*i) + " to " + query.get(2*i+1) + ": " + results[i]);
        }
        try{
            PrintWriter solFile = new PrintWriter("output.sol");
            for(int i = 0; i < results.length; i++){
                solFile.println(results[i]);
            }
            solFile.close();
        }catch (Exception e){
            System.out.println(e.getClass());
        }

    }

    public static void main(String args[]) {
        Graph graph = new Graph(args[1]);
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
        }

    }
}

