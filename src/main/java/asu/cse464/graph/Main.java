package asu.cse464.graph;

public class Main {

    public static void main(String[] args) throws Exception {

        Graph graph = new Graph();

        // Use the same test file
        String inputPath = "src/test/resources/input1.dot";

        graph.parseGraph(inputPath);

        // PRINT HERE
        System.out.println(graph.toString());
    }
}