package asu.cse464.graph.search;

import asu.cse464.graph.Graph;
import asu.cse464.graph.Path;

import java.util.*;

public abstract class GraphSearchTemplate {

    protected Graph graph;

    public GraphSearchTemplate(Graph graph) {
        this.graph = graph;
    }

    public Path search(String src, String dst) {

        if (!graph.getNodes().contains(src) || !graph.getNodes().contains(dst)) {
            return null;
        }

        Collection<List<String>> structure = createStructure();
        Set<String> visited = new HashSet<>();

        List<String> startPath = new ArrayList<>();
        startPath.add(src);

        add(structure, startPath);
        visited.add(src);

        while (!structure.isEmpty()) {

            List<String> currentPath = remove(structure);
            String lastNode = currentPath.get(currentPath.size() - 1);

            if (lastNode.equals(dst)) {
                return new Path(currentPath);
            }

            for (String neighbor : graph.getNeighbors(lastNode)) {
                if (!visited.contains(neighbor)) {

                    visited.add(neighbor);

                    List<String> newPath = new ArrayList<>(currentPath);
                    newPath.add(neighbor);

                    add(structure, newPath);
                }
            }
        }

        return null;
    }

    protected abstract Collection<List<String>> createStructure();
    protected abstract void add(Collection<List<String>> structure, List<String> path);
    protected abstract List<String> remove(Collection<List<String>> structure);
}