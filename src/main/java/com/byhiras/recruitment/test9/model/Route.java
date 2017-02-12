package com.byhiras.recruitment.test9.model;

import java.util.ArrayList;
import java.util.List;

public class Route {
    List<Station> path = new ArrayList();

    public int cost(){
        int cost = 0;
        String line = "";
        Station station1, station2;
        for(int i=0; i<path.size()-1; i++){
            station1 = path.get(i);
            station2 = path.get(i+1);
            if(i==0){
                line = station1.getInterchange(station2.getName());
            }
            cost += station1.cost(station2.getName(), line);
            line = station1.getInterchange(station2.getName());
        }
        return cost;
    }

    public void addStation(Station station){
        path.add(station);
    }

    public List<String> getPath(){
        List<String> strPath = new ArrayList();
        for(Station station : path){
            strPath.add(station.getName());
        }
        return strPath;
    }
}
