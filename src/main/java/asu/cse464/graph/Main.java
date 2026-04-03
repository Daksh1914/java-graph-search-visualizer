package asu.cse464.graph;

public class Main {

    public static void main(String[] args) {

        try {
            Graph graph = new Graph();

            System.out.println("=== Feature 1: Parse DOT ===");
            graph.parseGraph("src/test/resources/input1.dot");
            System.out.println(graph);

            System.out.println("\n=== Feature 2: Add Nodes ===");
            graph.addNode("x");
            graph.addNodes(new String[]{"y", "z"});
            System.out.println(graph);

            System.out.println("\n=== Feature 3: Add Edge ===");
            graph.addEdge("x", "y");
            System.out.println(graph);

            System.out.println("\n=== Feature 4: Output Files ===");

            graph.outputDOTGraph("output.dot");
            boolean success = graph.outputGraphics("output.png", "png");

            if (success) {
                System.out.println("PNG file generated successfully: output.png");
            } else {
                System.out.println("Failed to generate PNG file.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}