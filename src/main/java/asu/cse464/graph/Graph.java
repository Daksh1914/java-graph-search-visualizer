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

    // Feature 2: add a single node
    public boolean addNode(String label) {
        if (label == null || label.trim().isEmpty()) {
            return false;
        }
        return nodes.add(label.trim());
    }

    // Feature 2: add multiple nodes
    public void addNodes(String[] labels) {
        if (labels == null) return;
        for (String label : labels) {
            addNode(label);
        }
    }

    // Feature 3: add edge
    public boolean addEdge(String srcLabel, String dstLabel) {

        if (srcLabel == null || dstLabel == null) return false;

        srcLabel = srcLabel.trim();
        dstLabel = dstLabel.trim();

        if (!nodes.contains(srcLabel) || !nodes.contains(dstLabel)) {
            return false;
        }

        return edges.add(formatEdge(srcLabel, dstLabel));
    }

    // alias
    public void outputGraph(String path) throws IOException {
        outputDOTGraph(path);
    }

    // Feature 4: Graphviz
    public boolean outputGraphics(String path, String format) {

        if (format == null || !format.equalsIgnoreCase("png")) {
            return false;
        }

        try {
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

            Files.deleteIfExists(Paths.get(tempDotPath));

            return exitCode == 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeNode(String label) {
        if (!nodes.contains(label)) {
            throw new RuntimeException("Node does not exist: " + label);
        }

        nodes.remove(label);

        edges.removeIf(edge -> {
            String[] parts = parseEdge(edge);
            String src = parts[0];
            String dst = parts[1];
            return src.equals(label) || dst.equals(label);
        });
    }

    public void removeNodes(String[] labels) {
        for (String label : labels) {
            if (!nodes.contains(label)) {
                throw new RuntimeException("Node does not exist: " + label);
            }
        }

        for (String label : labels) {
            removeNode(label);
        }
    }

    public void removeEdge(String srcLabel, String dstLabel) {
        String target = formatEdge(srcLabel, dstLabel);

        if (!edges.contains(target)) {
            throw new RuntimeException("Edge does not exist: " + target);
        }

        edges.remove(target);
    }

    public Set<String> getNodes() {
        return nodes;
    }

    public Set<String> getEdges() {
        return edges;
    }

    // BFS
    public Path graphSearch(String src, String dst) {

        if (!isValidNode(src) || !isValidNode(dst)) {
            return null;
        }

        Queue<List<String>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        List<String> startPath = new ArrayList<>();
        startPath.add(src);

        queue.add(startPath);
        visited.add(src);

        while (!queue.isEmpty()) {

            List<String> currentPath = queue.poll();
            String lastNode = currentPath.get(currentPath.size() - 1);

            if (lastNode.equals(dst)) {
                return new Path(currentPath);
            }

            for (String neighbor : getNeighbors(lastNode)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);

                    List<String> newPath = new ArrayList<>(currentPath);
                    newPath.add(neighbor);

                    queue.add(newPath);
                }
            }
        }

        return null;
    }

    // DFS
    public Path graphSearchDFS(String src, String dst) {

        if (!isValidNode(src) || !isValidNode(dst)) {
            return null;
        }

        Set<String> visited = new HashSet<>();
        List<String> path = new ArrayList<>();

        if (dfsHelper(src, dst, visited, path)) {
            return new Path(path);
        }

        return null;
    }

    private boolean dfsHelper(String current, String dst, Set<String> visited, List<String> path) {

        visited.add(current);
        path.add(current);

        if (current.equals(dst)) {
            return true;
        }

        for (String to : getNeighbors(current)) {
            if (!visited.contains(to)) {
                if (dfsHelper(to, dst, visited, path)) {
                    return true;
                }
            }
        }

        path.remove(path.size() - 1);
        return false;
    }

    // STRATEGY ENTRY
    public Path graphSearch(String src, String dst, Algorithm algo) {

        if (algo == Algorithm.BFS) {
            return graphSearch(src, dst);
        } else {
            return graphSearchDFS(src, dst);
        }
    }

    //HELPER
    private String[] parseEdge(String edge) {
        String[] parts = edge.split("->");
        return new String[]{parts[0].trim(), parts[1].trim()};
    }

    public List<String> getNeighbors(String node) {
        List<String> neighbors = new ArrayList<>();

        for (String edge : edges) {
            String[] parts = parseEdge(edge);
            String source = parts[0];
            String dest = parts[1];

            if (source.equals(node)) {
                neighbors.add(dest);
            }
        }

        return neighbors;
    }

    private boolean isValidNode(String node) {
        return node != null && nodes.contains(node);
    }

    private String formatEdge(String src, String dst) {
        return src + " -> " + dst;
    }
}