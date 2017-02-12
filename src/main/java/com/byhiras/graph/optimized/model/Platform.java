package com.byhiras.graph.optimized.model;

public class Platform {

    private String line;
    private Station station;

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
