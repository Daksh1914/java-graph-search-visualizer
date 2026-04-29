package asu.cse464.graph.search;

import asu.cse464.graph.Path;
import asu.cse464.graph.Graph;

import java.util.*;

public class RandomWalkSearch extends GraphSearchTemplate implements SearchStrategy {

    private Random random = new Random();

    public RandomWalkSearch(Graph graph) {
        super(graph);
    }

    @Override
    protected Collection<List<String>> createStructure() {
        return new LinkedList<>();
    }

    @Override
    protected void add(Collection<List<String>> structure, List<String> path) {
        ((Queue<List<String>>) structure).add(path);
    }

    @Override
    protected List<String> remove(Collection<List<String>> structure) {
        return ((Queue<List<String>>) structure).poll();
    }

    @Override
    public Path search(String src, String dst) {

        if (!graph.getNodes().contains(src) || !graph.getNodes().contains(dst)) {
            return null;
        }

        List<String> path = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        String current = src;
        path.add(current);
        visited.add(current);

        while (!current.equals(dst)) {

            List<String> neighbors = graph.getNeighbors(current);

            if (neighbors.isEmpty()) {
                return null;
            }

            // pick random neighbor
            String next = neighbors.get(random.nextInt(neighbors.size()));

            if (!visited.contains(next)) {
                path.add(next);
                visited.add(next);
                current = next;
            } else {
                // avoid infinite loop by breaking
                return null;
            }
        }

        return new Path(path);
    }
}