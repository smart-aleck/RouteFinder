package com.byhiras.recruitment.test9.model;

import java.util.*;

public class TubeLines {

    private Map<String, TubeLine> tubeLinesMap = new HashMap();

    public void add(String tubeLine, List<String> stations){
        if(!tubeLinesMap.containsKey(tubeLine))
            tubeLinesMap.put(tubeLine, new TubeLine(stations));
        else tubeLinesMap.get(tubeLine).addBranch(stations);
    }

    // Tube Lines that intersect with each other
    public void addDirectConnections(Stations stations){
        for(TubeLine tubeLine : tubeLinesMap.values()){
            tubeLine.addDirectConnections(stations);
        }
    }
    // Check if the 2 Tube Lines intersect
    public boolean isPathPossible(Set<String> sourceConnections, Set<String> destinationConnections){
        return isPathPossible(sourceConnections, destinationConnections, new HashSet());
    }
    // This function is called recursively so 'skip' indicates the lines we have already gone through
    public boolean isPathPossible(Set<String> sourceConnections, Set<String> destinationConnections, Set<String> skip){

        sourceConnections.removeAll(skip);

        for(String source : sourceConnections){
            // Huzzah! We have found a connection
            if(destinationConnections.contains(source)) {
                return true;
            }
        }
        skip.addAll(sourceConnections);
        for(String source : sourceConnections){
            if(isPathPossible(tubeLinesMap.get(source).getDirectConnections(), destinationConnections, skip)) {
                return true;
            }
        }
        return false;
    }
}
