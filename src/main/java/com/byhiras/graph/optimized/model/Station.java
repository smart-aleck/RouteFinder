package com.byhiras.graph.optimized.model;

import java.util.*;

public class Station {

    final String name;
    private List<Platform> platforms = new ArrayList<Platform>();
    private List<Station> adjacentStations = new ArrayList<Station>();

    public Station(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public List<Platform> getPlatforms(){
        return platforms;
    }
    public void addPlatform(Platform platformToAdd){
        for(Platform platform : platforms)
            if(platformToAdd.equals(platform))
                return;

        platforms.add(platformToAdd);
    }

    public List<Station> getAdjacentStations(Set<Station> visitedStations){
        List<Station> nonVisitedAdjacentStations = new ArrayList<Station>();
        for(Station station : adjacentStations)
            if(!visitedStations.contains(station))
                nonVisitedAdjacentStations.add(station);

        return nonVisitedAdjacentStations;
    }

    public void addAdjacentStation(Station adjacentStation){
        if(adjacentStation == null)
            return;

        for(Station station : adjacentStations)
            if(station.equals(adjacentStation))
                return;

        adjacentStations.add(adjacentStation);
    }

    @Override
    public boolean equals(Object station){
        if (station == null) {
            return false;
        }
        if(!(station instanceof Station))
            return false;

        if(station == this)
            return true;

        return ((Station)station).getName().equals(this.getName());
    }
}
