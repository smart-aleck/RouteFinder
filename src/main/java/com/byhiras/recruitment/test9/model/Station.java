package com.byhiras.recruitment.test9.model;

import java.util.*;

public class Station {

    private String name;
    private Interchanges interchanges = new Interchanges();

    public Station(String station, String tubeLine, List<String> adjacentStations){
        name = station;
        addInterchange(tubeLine, adjacentStations);
    }
    public void addInterchange(String tubeLine, List<String> adjacentStations){
        interchanges.add(tubeLine, adjacentStations);
    }

    // Returns the Tube Line on which the adjacent station exists
    public String getInterchange(String adjacentStaion){
        for(Interchange interchange : interchanges.getInterchanges()){
            if(interchange.getAdjacentStations().contains(adjacentStaion)) {
                return interchange.getTubeLine();
            }
        }
        throw new RuntimeException(String.format("Could not get interchange between '%s' and '%s'", name, adjacentStaion));
    }

    // Calculating cost as per requirements
    public int cost(String adjacentStaion, String fromLine){
        if(getInterchange(adjacentStaion).equals(fromLine))
            return 2;   // 2 (station change)
        else return 6;  // 4 (interchange) + 2 (station change)
    }

    public Set<String> getAllAdjacentStations(){
        Set<String> adjacentStations = new HashSet();
        for(Interchange interchange : interchanges.getInterchanges()){
            adjacentStations.addAll(interchange.getAdjacentStations());
        }
        return adjacentStations;
    }
    public Set<String> getInterchanges(){
        return interchanges.getInterchangeNames();
    }

    public String getName() {
        return name;
    }
}
