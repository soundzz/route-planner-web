import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;


public class Hub {
    private List<Integer> query = new ArrayList<>(); //in pairs of start/target nodes at even/odd indices
    private int[] results;

    public void query(String fileName, Graph graph) {

        //read query from file:
        BufferedReader reader;
        try {

            reader = new BufferedReader(new FileReader(fileName));
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
            results[i] = graph.pathCost(query.get(2*i), query.get(2*i+1));
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
        Graph graph = new Graph("stgtregbz.fmi");
        Hub hub = new Hub();
        hub.query("Benchs/stgtregbz.que", graph);

    }
}

