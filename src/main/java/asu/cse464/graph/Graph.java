package asu.cse464.graph;

import asu.cse464.graph.search.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Graph {

    private final Set<String> nodes = new TreeSet<>();
    private final Set<String> edgeSet = new TreeSet<>();
    // ===========================
    // PARSE GRAPH
    // ===========================
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
                edgeSet.add(formatEdge(src, dst));
            }
        }
    }

    // ===========================
    // PRINT GRAPH
    // ===========================
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Number of nodes: ").append(nodes.size()).append("\n");
        sb.append("Nodes: ").append(nodes).append("\n");
        sb.append("Number of edges: ").append(edgeSet.size()).append("\n");

        int count = 0;
        for (String edge : edgeSet) {
            sb.append(edge);
            count++;
            if (count < edgeSet.size()) sb.append("\n");
        }

        return sb.toString();
    }

    // ===========================
    // OUTPUT DOT
    // ===========================
    public void outputDOTGraph(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {\n");

        for (String edge : edgeSet) {
            sb.append("    ").append(edge).append(";\n");
        }

        sb.append("}");
        Files.writeString(Paths.get(path), sb.toString());
    }

    public void outputGraph(String path) throws IOException {
        outputDOTGraph(path);
    }

    // ===========================
    // GRAPHVIZ OUTPUT
    // ===========================
    public boolean outputGraphics(String path, String format) {

        if (format == null || !format.equalsIgnoreCase("png")) return false;

        try {
            String tempDot = "temp_graph.dot";
            outputDOTGraph(tempDot);

            ProcessBuilder pb = new ProcessBuilder(
                    "dot",
                    "-T" + format,
                    tempDot,
                    "-o",
                    path
            );

            pb.redirectErrorStream(true);
            Process process = pb.start();
            int exitCode = process.waitFor();

            Files.deleteIfExists(Paths.get(tempDot));
            return exitCode == 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===========================
    // ADD / REMOVE
    // ===========================
    public boolean addNode(String label) {
        if (label == null || label.trim().isEmpty()) return false;
        return nodes.add(label.trim());
    }

    public void addNodes(String[] labels) {
        if (labels == null) return;
        for (String l : labels) addNode(l);
    }

    public boolean addEdge(String src, String dst) {

        if (src == null || dst == null) return false;

        src = src.trim();
        dst = dst.trim();

        if (!nodes.contains(src) || !nodes.contains(dst)) return false;

        return edgeSet.add(formatEdge(src, dst));
    }

    public void removeNode(String label) {
        if (!nodes.contains(label)) {
            throw new RuntimeException("Node does not exist: " + label);
        }

        nodes.remove(label);

        edgeSet.removeIf(edge -> {
            String[] parts = parseEdge(edge);
            return parts[0].equals(label) || parts[1].equals(label);
        });
    }

    public void removeNodes(String[] labels) {
        for (String l : labels) {
            if (!nodes.contains(l)) {
                throw new RuntimeException("Node does not exist: " + l);
            }
        }
        for (String l : labels) removeNode(l);
    }

    public void removeEdge(String src, String dst) {
        String target = formatEdge(src, dst);

        if (!edgeSet.contains(target)) {
            throw new RuntimeException("Edge does not exist: " + target);
        }

        edgeSet.remove(target);
    }

    // ===========================
    // STRATEGY PATTERN ENTRY
    // ===========================
    public Path graphSearch(String src, String dst, Algorithm algo) {

        SearchStrategy strategy;

        if (algo == Algorithm.BFS) {
            strategy = new BFSSearch(this);
        } else if (algo == Algorithm.DFS) {
            strategy = new DFSSearch(this);
        } else {
            strategy = new RandomWalkSearch(this);
        }

        return strategy.search(src, dst);
    }

    // ===========================
    // HELPERS
    // ===========================
    private String[] parseEdge(String edge) {
        String[] parts = edge.split("->");
        return new String[]{parts[0].trim(), parts[1].trim()};
    }

    public List<String> getNeighbors(String node) {
        List<String> neighbors = new ArrayList<>();

        for (String edge : edgeSet) {
            String[] parts = parseEdge(edge);
            if (parts[0].equals(node)) {
                neighbors.add(parts[1]);
            }
        }

        return neighbors;
    }

    private String formatEdge(String src, String dst) {
        return src + " -> " + dst;
    }

    public Set<String> getNodes() {
        return nodes;
    }

    public Set<String> getEdges() {
        return edgeSet;
    }
}