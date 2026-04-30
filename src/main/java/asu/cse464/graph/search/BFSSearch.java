package asu.cse464.graph.search;

import asu.cse464.graph.Graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BFSSearch extends GraphSearchTemplate {

    private Queue<List<String>> queue;

    public BFSSearch(Graph graph) {
        super(graph);
        this.queue = new LinkedList<>();
    }

    @Override
    protected void clearSearchStructure() {
        queue.clear();
    }

    @Override
    protected void addPath(List<String> path) {
        queue.add(path);
    }

    @Override
    protected List<String> getNextPath() {
        return queue.poll();
    }

    @Override
    protected boolean hasMorePaths() {
        return !queue.isEmpty();
    }
}