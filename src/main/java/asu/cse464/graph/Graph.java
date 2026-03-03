package asu.cse464.graph;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Graph {

    private final Set<String> nodes = new TreeSet<>();
    private final Set<String> edges = new TreeSet<>();

    // Feature 1 API
    public void parseGraph(String filepath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filepath));

        for (String line : lines) {
            line = line.trim();

            if (line.contains("->")) {
                String[] parts = line.replace(";", "").split("->");
                String src = parts[0].trim();
                String dst = parts[1].trim();
                nodes.add(src);
                nodes.add(dst);
                edges.add(src + " -> " + dst);
            }
        }
    }

    // Feature 1: print graph
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Number of nodes: ").append(nodes.size()).append("\n");
        sb.append("Nodes: ").append(nodes).append("\n");
        sb.append("Number of edges: ").append(edges.size()).append("\n");

        int count = 0;
        for (String edge : edges) {
            sb.append(edge);
            count++;
            if (count < edges.size()) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    // Feature 1: output DOT file
    public void outputDOTGraph(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {\n");

        for (String edge : edges) {
            sb.append("    ").append(edge).append(";\n");
        }

        sb.append("}");

        Files.writeString(Paths.get(path), sb.toString());
    }


    // Feature 2: add a single node with duplicate check
    public boolean addNode(String label) {
        if (label == null || label.trim().isEmpty()) {
            return false;
        }

        return nodes.add(label.trim()); // returns false if duplicate
    }

    // Feature 2: add multiple nodes
    public void addNodes(String[] labels) {
        if (labels == null) return;

        for (String label : labels) {
            addNode(label);
        }
    }

    // Feature 3: add edge with duplicate checking
    public boolean addEdge(String srcLabel, String dstLabel) {

        if (srcLabel == null || dstLabel == null) {
            return false;
        }

        srcLabel = srcLabel.trim();
        dstLabel = dstLabel.trim();

        // Both nodes must exist before adding edge
        if (!nodes.contains(srcLabel) || !nodes.contains(dstLabel)) {
            return false;
        }

        return edges.add(srcLabel + " -> " + dstLabel);
    }

    // required alias
    public void outputGraph(String path) throws IOException {
        outputDOTGraph(path);
    }

    // Feature 4: output graphics using Graphviz
    public boolean outputGraphics(String path, String format) {

        if (format == null || !format.equalsIgnoreCase("png")) {
            return false; // Only png required
        }

        try {
            // Create temporary DOT file
            String tempDotPath = "temp_graph.dot";
            outputDOTGraph(tempDotPath);

            ProcessBuilder pb = new ProcessBuilder(
                    "dot",
                    "-T" + format,
                    tempDotPath,
                    "-o",
                    path
            );

            pb.redirectErrorStream(true);
            Process process = pb.start();
            int exitCode = process.waitFor();

            // Clean up temporary file
            Files.deleteIfExists(Paths.get(tempDotPath));

            return exitCode == 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}