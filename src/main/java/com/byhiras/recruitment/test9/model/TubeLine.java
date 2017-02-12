package com.byhiras.recruitment.test9.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TubeLine {

    private List<TubeBranch> lines = new ArrayList();
    private Set<String> directConnections = new HashSet();

    public TubeLine(List<String> stations){
        lines.add(new TubeBranch(stations));
    }
    public void addBranch(List<String> stations){
        lines.add(new TubeBranch(stations));
    }

    // All Tube Lines that the current one intersects with on any of stations
    public void addDirectConnections(Stations stations){
        for(TubeBranch line : lines){
            for(String station : line.getStations()){
                getDirectConnections().addAll(stations.get(station).getInterchanges());
            }
        }
    }

    public Set<String> getDirectConnections() {
        return directConnections;
    }

    // NESTED CLASS 'TubeBranch'
    private class TubeBranch {
        private List<String> stations = new ArrayList();

        public TubeBranch(List<String> stns) {
            stations.addAll(stns);
        }

        public List<String> getStations() {
            return stations;
        }
    }
}

