package com.byhiras.recruitment.test9.model;

import java.util.ArrayList;
import java.util.List;

// This refers to the Tube Line for a station where you can
// interchange to other stations
public class Interchange {
    private String tubeLine;
    private List<String> adjacentStations = new ArrayList();

    public Interchange(String tubeLine, List<String> adjacentStns){
        this.tubeLine = tubeLine;
        addInterchange(adjacentStns);
    }
    public void addInterchange(List<String> adjacentStns){
        getAdjacentStations().addAll(adjacentStns);
    }

    public List<String> getAdjacentStations() {
        return adjacentStations;
    }

    public String getTubeLine() {
        return tubeLine;
    }
}
