package asu.cse464.graph.search;

import asu.cse464.graph.Graph;
import asu.cse464.graph.Path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class GraphSearchTemplate implements SearchStrategy {

    protected Graph graph;

    public GraphSearchTemplate(Graph graph) {
        this.graph = graph;
    }

    @Override
    public Path search(String src, String dst) {
        clearSearchStructure();

        List<String> startPath = new ArrayList<>();
        startPath.add(src);
        addPath(startPath);

        Set<String> visited = new HashSet<>();

        while (hasMorePaths()) {
            List<String> currentPath = getNextPath();

            System.out.println("visiting " + formatPath(currentPath));

            String currentNode = currentPath.get(currentPath.size() - 1);

            if (currentNode.equals(dst)) {
                return new Path(currentPath);
            }

            if (visited.contains(currentNode)) {
                continue;
            }

            visited.add(currentNode);

            for (String neighbor : graph.getNeighbors(currentNode)) {
                if (!visited.contains(neighbor)) {
                    List<String> newPath = new ArrayList<>(currentPath);
                    newPath.add(neighbor);
                    addPath(newPath);
                }
            }
        }

        return null;
    }

    protected String formatPath(List<String> path) {
        return String.join(" -> ", path);
    }

    protected abstract void clearSearchStructure();

    protected abstract void addPath(List<String> path);

    protected abstract List<String> getNextPath();

    protected abstract boolean hasMorePaths();
}