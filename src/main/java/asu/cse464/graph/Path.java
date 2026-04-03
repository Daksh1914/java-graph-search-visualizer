package asu.cse464.graph;

import java.util.*;

public class Path {

    private List<String> nodes;

    public Path(List<String> nodes) {
        this.nodes = nodes;
    }

    public List<String> getNodes() {
        return nodes;
    }

    @Override
    public String toString() {
        return String.join(" -> ", nodes);
    }
}