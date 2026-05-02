package asu.cse464.graph.search;

import asu.cse464.graph.Graph;

import java.util.List;
import java.util.Stack;

public class DFSSearch extends GraphSearchTemplate {

    private Stack<List<String>> stack;

    public DFSSearch(Graph graph) {
        super(graph);
        this.stack = new Stack<>();
    }

    @Override
    protected void clearSearchStructure() {
        stack.clear();
    }

    @Override
    protected void addPath(List<String> path) {
        stack.push(path);
    }

    @Override
    protected List<String> getNextPath() {
        return stack.pop();
    }

    @Override
    protected boolean hasMorePaths() {
        return !stack.isEmpty();
    }
}