package com.byhiras.graph.rudimentary.model;

import java.util.ArrayList;
import java.util.List;

public class Route {

    List<Platform> platforms = new ArrayList<Platform>();

    public Route(List<Platform> platforms){
        this.platforms = new ArrayList<Platform>(platforms);
    }
    public Route(List<Platform> platforms, Platform nextPlatform){
        this.platforms = new ArrayList<Platform>(platforms);
        this.platforms.add(nextPlatform);
    }
    public List<Platform> getPlatforms(){
        return platforms;
    }
    public int calculateCost(){
        int cost = 0;
        for(int i=1; i<platforms.size(); i++){
            if(platforms.get(i-1).getStation().getName().equals(platforms.get(i).getStation().getName()))
                cost += 4;  // Interchange Line
            else cost += 2; // Next station on the same line
        }
        return cost;
    }

    public List<String> returnStationList(){
        List<String> stations = new ArrayList<String>();
        stations.add(platforms.get(0).getStation().getName());
        for(int i=1; i<platforms.size(); i++){
            if(!platforms.get(i-1).getStation().getName().equals(platforms.get(i).getStation().getName()))
                stations.add(platforms.get(i).getStation().getName());
        }
        return stations;
    }

    public void printRoute(){
        for(Platform platform : platforms){
            System.out.print(String.format("%s (%s) -> ", platform.getStation().getName(), platform.getLine()));
        }
        System.out.println();
    }
}
