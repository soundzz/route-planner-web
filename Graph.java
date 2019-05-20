import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.util.Locale;
import java.io.FileNotFoundException;

/**
 * the class Graph reads the File with the graph information
 * the array Nodes contains the nodes with length and width.
 * the array Edges contains the edges with starting node, target node and costs.
 * OffsetArray contains the offset, the first place of the edge in the array edges of a node.
 * numberOfNodes is the number of nodes in the graph.
 * numberOfEdges is the number of edges in the graph.
 *
 *
 * */
public class Graph {
    private int[] OffsetArray;
    private float[][] Nodes;
    private float[][] Edges;
    private int numberOfNodes;
    private int numberOfEdges;

    /**
     * readGraphData reads the file and saves the information in the arrays Nodes and Edges.
     * the methodes works in the following way:
     * 1) BufferedReader reads the file.
     * 2) the first lines of the file gets skipped
     * 3) a scanner gets created and goes through the lines
     *
     * If no file is found the programm throws an exception.
     * */

    public void readGraphData(String fileName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));


            boolean dataStart = false;
            String line = null;
            while (!dataStart) {
                line = reader.readLine();
                if(!line.contains("#")){
                    dataStart = true;
                }
            }

            numberOfNodes = Integer.parseInt(reader.readLine());
            numberOfEdges = Integer.parseInt(reader.readLine());
            Nodes = new float[numberOfNodes][2];
            Edges = new float[numberOfEdges][3];

            Scanner scanner = null;

            for(int i = 0; i < numberOfNodes; i++){
                scanner = new Scanner(reader.readLine());

                scanner.next();
                scanner.next();

                Nodes[i][0] = Float.parseFloat(scanner.next()); //Latitude
                Nodes[i][1] = Float.parseFloat(scanner.next()); //Longitude
            }
            scanner.close();
            for(int i = 0; i < numberOfEdges; i++){
                scanner = new Scanner(reader.readLine());
                Edges[i][0] = Float.parseFloat(scanner.next()); //srcID
                Edges[i][1] = Float.parseFloat(scanner.next()); //trgID
                Edges[i][2] = Float.parseFloat(scanner.next()); //cost
            }
            scanner.close();



        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * calculateOffset creates the Offset Array
     * */


    public void calculateOffset() {
        OffsetArray = new int[numberOfNodes];
        for (int i = 0; i < numberOfEdges; i++) {
            if(i==0 || (Edges[i][0] != Edges[i-1][0])){
                OffsetArray[(int)Edges[i][0]] =    i;
            }

        }



    }

    /**
     * Method for system out.
     * */
        public void printNodes(){
            for(int i = 0; i < numberOfNodes; i++){
               // System.out.println("Node ID: " + i + " | x: " + Nodes[i][0] + " | y: " + Nodes[i][1]);
                System.out.println("Offset: "+ i+ "  Eintrag "+ OffsetArray[i]);
            }
        }


    /**
     * Main
     * */
        public static void main (String[]args){
            Graph graph = new Graph();
            graph.readGraphData("toy.fmi");
            graph.calculateOffset();
            graph.printNodes();

        }
    }
