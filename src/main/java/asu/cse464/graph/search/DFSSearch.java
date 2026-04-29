package asu.cse464.graph.search;

import asu.cse464.graph.Graph;

import java.util.*;

public class DFSSearch extends GraphSearchTemplate {

    public DFSSearch(Graph graph) {
        super(graph);
    }

    @Override
    protected Collection<List<String>> createStructure() {
        return new Stack<>(); // Stack
    }

    @Override
    protected void add(Collection<List<String>> structure, List<String> path) {
        ((Stack<List<String>>) structure).push(path);
    }

    @Override
    protected List<String> remove(Collection<List<String>> structure) {
        return ((Stack<List<String>>) structure).pop();
    }
}