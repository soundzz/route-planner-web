import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.util.Locale;
import java.io.FileNotFoundException;

public class Graph {
    private int[] OffsetArray;
    private float[][] Nodes;
    private float[][] Vertices;
    private int numberOfNodes;
    private int numberOfVertices;

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
            numberOfVertices = Integer.parseInt(reader.readLine());
            Nodes = new float[numberOfNodes][2];
            Vertices = new float[numberOfVertices][3];

            Scanner scanner = null;

            for(int i = 0; i < numberOfNodes; i++){
                scanner = new Scanner(reader.readLine());

                scanner.next();
                scanner.next();

                Nodes[i][0] = Float.parseFloat(scanner.next()); //Latitude
                Nodes[i][1] = Float.parseFloat(scanner.next()); //Longitude
            }
            scanner.close();
            for(int i = 0; i < numberOfVertices; i++){
                scanner = new Scanner(reader.readLine());
                Vertices[i][0] = Float.parseFloat(scanner.next()); //srcID
                Vertices[i][1] = Float.parseFloat(scanner.next()); //trgID
                Vertices[i][2] = Float.parseFloat(scanner.next()); //cost
            }
            scanner.close();



        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void printNodes(){
        for(int i = 0; i < numberOfNodes; i++){
            System.out.println("Node ID: " + i + " | x: " + Nodes[i][0] + " | y: " + Nodes[i][1]);
        }
    }
        public static void main (String[]args){
            Graph graph = new Graph();
            graph.readGraphData("toy.fmi");
            graph.printNodes();
        }
    }
