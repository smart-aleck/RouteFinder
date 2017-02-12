package com.byhiras.graph.rudimentary.model;

import java.util.*;

public class Station {

    final String name;
    Map<String, Platform> platforms = new HashMap<String, Platform>();

    public Station(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public List<Platform> getPlatforms(){
        List<Platform> platformsList = new ArrayList<Platform>();

        Iterator platformIterator = platforms.entrySet().iterator();
        while(platformIterator.hasNext()) {
            Map.Entry<String, Platform> platformPair = (Map.Entry)platformIterator.next();
            platformsList.add(platformPair.getValue());
        }
        return platformsList;
    }

    public Platform addPlatform(Platform platform){
        if(!platforms.containsKey(platform.getLine()))
            platforms.put(platform.getLine(), platform);
        return platforms.get(platform.getLine());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
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
