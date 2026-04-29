package asu.cse464.graph.search;

import asu.cse464.graph.Path;

public interface SearchStrategy {
    Path search(String src, String dst);
}