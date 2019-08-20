package roupla.utility;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

public class Hub{
    private List<Integer> query = new ArrayList<>(); //in pairs of start/target nodes at even/odd indices
    private int[] results;
    Graph graph;
    Tree KDTree;
    public Hub(String path){
        graph = new Graph(path);
        KDTree = new Tree(graph.getNodes());
    }
    public static String test(){
        return "hello world";
    }

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
        Hub hub = new Hub("MV.fmi");

    }
}

