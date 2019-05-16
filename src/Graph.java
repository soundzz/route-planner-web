import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;

/**
 * Die Klasse Graph liest die verschiedenen Graphen ein.
 * In Vertices werden die Knoten mit Länge und Breite gespeichert.
 * In Edges werden die Kanten mit Startknoten, Zielknoten und Kosten gespeichert.
 * In OffsetArray wird der Offset, die erste Kante, die von einem Knoten abgeht, gespeichert.
 * numberOfVertices ist die Anzahl der Knoten
 * numberOfEdges ist die Anzahl der Kanten
 */
public class Graph {
    private int[] OffsetArray;
    private float[][] Vertices;
    private float[][] Edges;
    private int numberOfVertices;
    private int numberOfEdges;

    /**
     * liest die Datei des Graphen ein und speichert die Daten in den Arrays.
     * BufferedReader liest das File ein.
     * Dann werden die ersten Zeilen der Datei uebersprungen.
     * ein Scanner wird erstellt um durch die Zeilen zu gehen.
     * Falls die Datei nicht gefunden wird, wird eine Exception geworfen.
     *
     * @param fileName specifies the file containing graph data
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

            numberOfVertices = Integer.parseInt(reader.readLine());
            numberOfEdges = Integer.parseInt(reader.readLine());
            Vertices = new float[numberOfVertices][2];
            Edges = new float[numberOfEdges][3];

            Scanner scanner = null;

            for (int i = 0; i < numberOfVertices; i++) {
                scanner = new Scanner(reader.readLine());

                scanner.next();
                scanner.next();

                Vertices[i][0] = Float.parseFloat(scanner.next()); //Latitude
                Vertices[i][1] = Float.parseFloat(scanner.next()); //Longitude
            }
            scanner.close();
            for (int i = 0; i < numberOfEdges; i++) {
                scanner = new Scanner(reader.readLine());
                Edges[i][0] = Float.parseFloat(scanner.next()); //srcID
                Edges[i][1] = Float.parseFloat(scanner.next()); //trgID
                Edges[i][2] = Float.parseFloat(scanner.next()); //cost
            }
            scanner.close();


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * erstellt das Offset Array
     */
    public void calculateOffset() { //muss nicht public sein? sollte nur beim einlesen innerhalb der klasse aufgerufen werden
        OffsetArray = new int[numberOfVertices];
        for (int i = 0; i < numberOfEdges; i++) {
            if (Edges[i][0] == i) {
                OffsetArray[(int) Edges[i][0]] = i; //evtl etwas umständlich, dauert evtl zu lang
            }
            if (i > 0 && (Edges[i][0] != Edges[i - 1][0])) {
                OffsetArray[(int) Edges[i][0]] = i;
            }

        }


    }

    /**
     * Hilfsmethode zur Ausgabe
     */
    public void printNodes() {
        for (int i = 0; i < numberOfVertices; i++) {
            // System.out.println("Node ID: " + i + " | x: " + Vertices[i][0] + " | y: " + Vertices[i][1]);
            System.out.println("Offset: " + i + "  Eintrag " + OffsetArray[i]);
        }
    }


    /**
     * Main
     */
    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.readGraphData("toy.fmi");
        graph.calculateOffset();
        graph.printNodes();

    }
}
