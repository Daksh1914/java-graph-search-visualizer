package asu.cse464.graph.search;

import asu.cse464.graph.Graph;
import asu.cse464.graph.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomWalkSearch implements SearchStrategy {

    private Graph graph;
    private Random random;

    public RandomWalkSearch(Graph graph) {
        this.graph = graph;
        this.random = new Random();
    }

    @Override
    public Path search(String src, String dst) {
        List<String> pathList = new ArrayList<>();
        pathList.add(src);

        String currentNode = src;

        int maxSteps = 100;
        int steps = 0;

        while (steps < maxSteps) {
            System.out.println("visiting " + String.join(" -> ", pathList));

            if (currentNode.equals(dst)) {
                return new Path(pathList);
            }

            List<String> neighbors = new ArrayList<>(graph.getNeighbors(currentNode));

            if (neighbors.isEmpty()) {
                return null;
            }

            String nextNode = neighbors.get(random.nextInt(neighbors.size()));
            pathList.add(nextNode);
            currentNode = nextNode;

            steps++;
        }

        return null;
    }
}