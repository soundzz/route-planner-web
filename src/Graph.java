import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.util.Locale;
import java.io.FileNotFoundException;

/**
 * Die Klasse Graph liest die verschiedenen Graphen ein.
 * In Nodes werden die Knoten mit Länge und Breite gespeichert.
 * In Vertices werden die Kanten mit Startknoten, Zielknoten und Kosten gespeichert.
 * In OffsetArray wird der Offset, die erste Kante, die von einem Knoten abgeht, gespeichert.
 * numberOfNodes ist die Anzahl der Knoten
 * numberOfVertices ist die Anzahl der Kanten
 *
 *
 * */
public class Graph {
    private int[] OffsetArray;
    private float[][] Nodes;
    private float[][] Vertices; // ???? Ecken?
    private int numberOfNodes;
    private int numberOfVertices;

    /**
     * readGraphData liest die Datei des Graphen ein und speichert die Daten in den Arrays.
     * BufferedReader liest das File ein.
     * Dann werden die ersten Zeilen der Datei uebersprungen.
     * ein Scanner wird erstellt um durch die Zeilen zu gehen.
     * Falls die Datei nicht gefunden wird, wird eine Exeption geworfen.
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

    /**
     * calculateOffset erstellt das Offset Array
     * */


    public void calculateOffset() {
        OffsetArray = new int[numberOfNodes];
        for (int i = 0; i < numberOfVertices; i++) {
            if (Vertices[i][0] == i){
                OffsetArray[(int) Vertices[i][0]]=i;
            }
            if(i>0 && (Vertices[i][0] != Vertices[i-1][0])){
                OffsetArray[(int)Vertices[i][0]] =    i;
            }

        }



    }

    /**
     * Hilfsmethode zur Ausgabe
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
