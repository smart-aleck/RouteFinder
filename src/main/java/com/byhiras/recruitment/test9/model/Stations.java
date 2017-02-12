package com.byhiras.recruitment.test9.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stations {

    private Map<String, Station> stationMap = new HashMap();

    private void add(String station, String tubeLine, List<String> adjacentStations){
        if(!stationMap.containsKey(station))
            stationMap.put(station, new Station(station, tubeLine, adjacentStations));
        else stationMap.get(station).addInterchange(tubeLine, adjacentStations);
    }
    public Station get(String station){
        if(stationMap.get(station) == null) {
            throw new RuntimeException(String.format("Station '%s' does not exist!", station));
        }
        return stationMap.get(station);
    }

    public void addAll(List<String> stations, String tubeLine){
        List<String> adjacentStations;
        for(int i=0; i<stations.size(); i++) {
            adjacentStations = new ArrayList();

            // Getting the adjacent stations
            if(i>0) {
                adjacentStations.add(stations.get(i-1));
            }
            if(i<stations.size()-1) {
                adjacentStations.add(stations.get(i+1));
            }
            this.add(stations.get(i), tubeLine, adjacentStations);
        }
    }
}
