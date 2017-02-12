package com.byhiras.graph.optimized.model;

import java.util.ArrayList;
import java.util.List;

public class Route {

    List<Station> stations = new ArrayList<Station>();

    public Route(List<Station> stations){
        this.stations = new ArrayList<Station>(stations);
    }
    public Route(List<Station> stations, Station nextStation){
        this.stations = new ArrayList<Station>(stations);
        this.stations.add(nextStation);
    }
    public List<Station> getStations(){
        return stations;
    }
    public int calculateCost(){
        int cost = 2;

        // An interchange will be required at the current station only
        // if there is no common line between the previous and the next station
        for(int i=1; i<stations.toArray().length-1; i++)
            cost += findMinimumCost(stations.get(i-1), stations.get(i+1));

        return cost;
    }
    // An interchange will be required at the current station only
    // if there is no common line between the previous and the next station
    private int findMinimumCost(Station previousStation, Station nextStation){
        for(Platform previousPlatform : previousStation.getPlatforms())
            for(Platform nextPlatform : nextStation.getPlatforms())
                if(previousPlatform.getLine().equals(nextPlatform.getLine()))
                    return 2;

        // Interchange + Next station
        return 6;
    }

    public List<String> returnStationList(){
        List<String> stationNames = new ArrayList<String>();
        for(Station station : stations)
            stationNames.add(station.getName());

        return stationNames;
    }
}
