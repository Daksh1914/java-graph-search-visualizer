package asu.cse464.graph;

public class Main {

    public static void main(String[] args) {
        try {
            Graph graph = new Graph();

            System.out.println("=== Feature 1: Parse DOT ===");
            graph.parseGraph("src/test/resources/demo.dot");
            System.out.println(graph);

            System.out.println();
            System.out.println("=== Feature 2: Add Nodes ===");
            graph.addNode("x");
            graph.addNodes(new String[]{"y", "z"});
            System.out.println(graph);

            System.out.println();
            System.out.println("=== Feature 3: Add Edge ===");
            graph.addEdge("x", "y");
            System.out.println(graph);

            System.out.println();
            System.out.println("=== Feature 4: Output Files ===");
            graph.outputDOTGraph("output.dot");

            boolean pngCreated = graph.outputGraphics("output.png", "png");

            if (pngCreated) {
                System.out.println("PNG file generated successfully: output.png");
            } else {
                System.out.println("PNG file was not generated.");
            }

            System.out.println();
            System.out.println("=== Feature 5: BFS Search ===");
            System.out.println("BFS:");
            Path bfsPath = graph.graphSearch("a", "h", Algorithm.BFS);
            System.out.println("Path: " + bfsPath);

            System.out.println();
            System.out.println("=== Feature 6: DFS Search ===");
            System.out.println("DFS:");
            Path dfsPath = graph.graphSearch("a", "h", Algorithm.DFS);
            System.out.println("Path: " + dfsPath);

            System.out.println();
            System.out.println("=== Feature 7: Random Walk Search ===");
            System.out.println("Random Walk:");
            Path randomPath = graph.graphSearch("a", "h", Algorithm.RANDOM);
            System.out.println("Path: " + randomPath);

            System.out.println("Search completed");
            System.out.println("Graph image generated: output.png");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}