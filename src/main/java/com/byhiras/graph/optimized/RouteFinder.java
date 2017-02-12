package com.byhiras.graph.optimized;

import java.util.List;

public interface RouteFinder {
    List<String> anyOptimalRoute(String originStation, String destinationStation);
}