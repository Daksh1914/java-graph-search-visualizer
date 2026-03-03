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
}