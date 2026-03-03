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

        Graph graph = new Graph();

        graph.addNode("a");
        graph.addNode("b");
        graph.addEdge("a", "b");

        String outputPath = "test_output.png";

        boolean success = graph.outputGraphics(outputPath, "png");

        assertTrue(success);
        assertTrue(Files.exists(Paths.get(outputPath)));

        // Cleanup
        Files.deleteIfExists(Paths.get(outputPath));
    }
}