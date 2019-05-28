import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;


public class Hub {
    private List<Integer> query = new ArrayList<Integer>(); //in pairs of start/target nodes at even/odd indices
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
        System.out.println("a");
        for (int i = 0; i < results.length; i++){
            results[i] = graph.pathCost(query.get(i), query.get(i+1));
            System.out.println(results[i]);
        }

    }

    public static void main(String args[]) {
        Graph graph = new Graph("stgtregbz.fmi");
        System.out.println(graph.pathCost(377371, 754742));
    }
}

