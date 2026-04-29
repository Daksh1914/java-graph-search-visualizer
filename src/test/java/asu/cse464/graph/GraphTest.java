package asu.cse464.graph;

import org.junit.jupiter.api.Test;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Test
    void testParseGraph() throws Exception {
        Graph graph = new Graph();

        String inputPath = "src/test/resources/input1.dot";
        graph.parseGraph(inputPath);

        String output = graph.toString();
        String expected = Files.readString(Paths.get("src/test/resources/expected1.txt"));

        // Normalize line endings for Windows/Mac/Linux compatibility
        output = output.replace("\r\n", "\n").trim();
        expected = expected.replace("\r\n", "\n").trim();

        assertEquals(expected, output);
    }

    @Test
    void testAddNodeAndAddNodes() {

        Graph graph = new Graph();

        // Test addNode
        assertTrue(graph.addNode("x"));
        assertFalse(graph.addNode("x")); // duplicate

        // Test addNodes
        String[] labels = {"a", "b", "c", "a"};
        graph.addNodes(labels);

        String output = graph.toString();
        output = output.replace("\r\n", "\n").trim();

        assertTrue(output.contains("x"));
        assertTrue(output.contains("a"));
        assertTrue(output.contains("b"));
        assertTrue(output.contains("c"));
    }

    @Test
    void testAddEdge() {

        Graph graph = new Graph();

        graph.addNode("a");
        graph.addNode("b");

        // valid edge
        assertTrue(graph.addEdge("a", "b"));

        // duplicate edge
        assertFalse(graph.addEdge("a", "b"));

        // edge with missing node
        assertFalse(graph.addEdge("a", "c"));

        String output = graph.toString();
        output = output.replace("\r\n", "\n").trim();

        assertTrue(output.contains("a -> b"));
    }

    @Test
    void testOutputGraphics() throws Exception {

        // Skip test in CI (GitHub Actions)
        String ci = System.getenv("CI");
        if (ci != null && ci.equals("true")) {
            return;
        }

        Graph graph = new Graph();

        graph.addNode("a");
        graph.addNode("b");
        graph.addEdge("a", "b");

        String outputPath = "test_output.png";

        boolean success = graph.outputGraphics(outputPath, "png");

        assertTrue(success);
        assertTrue(Files.exists(Paths.get(outputPath)));

        Files.deleteIfExists(Paths.get(outputPath));
    }

    @Test
    public void testRemoveSuccess() {
        Graph g = new Graph();

        g.addNode("a");
        g.addNode("b");
        g.addNode("c");

        g.addEdge("a", "b");
        g.addEdge("b", "c");

        g.removeNode("b");

        assertFalse(g.getNodes().contains("b"));
        assertEquals(0, g.getEdges().size());
    }

    @Test
    public void testRemoveNodeNotExist() {
        Graph g = new Graph();
        g.addNode("a");

        assertThrows(RuntimeException.class, () -> {
            g.removeNode("x");
        });
    }

    @Test
    public void testRemoveEdgeNotExist() {
        Graph g = new Graph();

        g.addNode("a");
        g.addNode("b");

        assertThrows(RuntimeException.class, () -> {
            g.removeEdge("a", "b");
        });
    }

    @Test
    public void testRemoveMultipleNodes() {
        Graph g = new Graph();

        g.addNode("a");
        g.addNode("b");
        g.addNode("c");
        g.addNode("d");

        g.addEdge("a", "b");
        g.addEdge("b", "c");
        g.addEdge("c", "d");

        String[] toRemove = {"b", "c"};
        g.removeNodes(toRemove);

        assertFalse(g.getNodes().contains("b"));
        assertFalse(g.getNodes().contains("c"));

        // all edges involving b and c should be removed
        assertEquals(0, g.getEdges().size());
    }

    @Test
    public void testGraphSearch() {

        Graph g = new Graph();

        g.addNode("a");
        g.addNode("b");
        g.addNode("c");

        g.addEdge("a", "b");
        g.addEdge("b", "c");

        Path path = g.graphSearch("a", "c", Algorithm.BFS);

        assertNotNull(path);
        assertEquals("a -> b -> c", path.toString());
    }

    @Test
    public void testGraphSearchWithAlgorithm() throws Exception {
        Graph g = new Graph();

        g.addNode("A");
        g.addNode("B");
        g.addNode("C");
        g.addNode("D");

        g.addEdge("A", "B");
        g.addEdge("B", "C");
        g.addEdge("C", "D");

        Path bfsPath = g.graphSearch("A", "D", Algorithm.BFS);
        Path dfsPath = g.graphSearch("A", "D", Algorithm.DFS);

        assertNotNull(bfsPath);
        assertNotNull(dfsPath);
    }

}

