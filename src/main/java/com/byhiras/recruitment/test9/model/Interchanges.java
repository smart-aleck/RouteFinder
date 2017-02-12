package com.byhiras.recruitment.test9.model;

import java.util.*;

public class Interchanges {

    private Map<String, Interchange> interchangesMap = new HashMap();

    public void add(String tubeLine, List<String> adjacentStations){
        if(!interchangesMap.containsKey(tubeLine))
            interchangesMap.put(tubeLine, new Interchange(tubeLine, adjacentStations));
        else interchangesMap.get(tubeLine).addInterchange(adjacentStations);
    }
    public List<Interchange> getInterchanges(){
        return new ArrayList<Interchange>(interchangesMap.values());
    }
    public Set<String> getInterchangeNames(){
        return interchangesMap.keySet();
    }
}
