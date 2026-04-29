package asu.cse464.graph.search;

import asu.cse464.graph.search.SearchStrategy;
import asu.cse464.graph.Graph;

import java.util.*;

public class BFSSearch extends GraphSearchTemplate implements SearchStrategy {

    public BFSSearch(Graph graph) {
        super(graph);
    }

    @Override
    protected Collection<List<String>> createStructure() {
        return new LinkedList<>(); // Queue
    }

    @Override
    protected void add(Collection<List<String>> structure, List<String> path) {
        ((Queue<List<String>>) structure).add(path);
    }

    @Override
    protected List<String> remove(Collection<List<String>> structure) {
        return ((Queue<List<String>>) structure).poll();
    }
}