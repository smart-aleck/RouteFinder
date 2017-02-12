package com.byhiras.graph.rudimentary;

import java.util.List;

public interface RouteFinder {
    List<String> anyOptimalRoute(String originStation, String destinationStation);
}