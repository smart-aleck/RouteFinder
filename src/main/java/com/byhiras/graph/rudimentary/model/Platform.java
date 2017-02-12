package com.byhiras.graph.rudimentary.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Platform {

    private String line;
    private Station station;
    private List<Platform> adjacentPlatforms = new ArrayList<Platform>();

    public Platform(String line, Station station){
        this.line = line;
        this.station = station;
    }
    public String getLine(){
        return line;
    }
    public Station getStation(){
        return station;
    }
    public List<Platform> getAdjacentPlatforms(Set<Platform> visitedPlatforms){
        List<Platform> nonVisitedAdjacentPlatforms = new ArrayList<Platform>();
        for(Platform platform : adjacentPlatforms)
            if(!visitedPlatforms.contains(platform))
                nonVisitedAdjacentPlatforms.add(platform);

        return nonVisitedAdjacentPlatforms;
    }

    public void addAdjacentPlatform(Platform adjacentPlatform){
        addAdjacentPlatformIfDoesntExist(adjacentPlatform);
        for(Platform platform : station.getPlatforms()){
            if(!platform.equals(this)){
                platform.addAdjacentPlatformIfDoesntExist(this);
                this.addAdjacentPlatformIfDoesntExist(platform);

            }
        }
    }
    private void addAdjacentPlatformIfDoesntExist(Platform adjacentPlatform){
        if(adjacentPlatform == null)
            return;

        for(Platform platform : adjacentPlatforms)
            if(platform.equals(adjacentPlatform))
                return;

        adjacentPlatforms.add(adjacentPlatform);
    }

    @Override
    public int hashCode() {
        int result = line.hashCode();
        result = 31 * result + station.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object platform){
        if (platform == null) {
            return false;
        }
        if(!(platform instanceof Platform))
            return false;

        if(platform == this)
            return true;

        return ((Platform)platform).getLine().equals(this.getLine()) &&
                ((Platform) platform).getStation().equals(this.getStation());
    }

}
