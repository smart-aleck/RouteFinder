package com.byhiras;

import com.byhiras.graph.optimized.RouteFinderImpl;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        //List<String> route = new com.byhiras.recruitment.test9.RouteFinderImpl("network.txt").anyOptimalRoute("Oval","Southwark");
        //List<String> route = new com.byhiras.recruitment.test9.RouteFinderImpl("network1.txt").anyOptimalRoute("A","Z");

        List<String> route = new RouteFinderImpl("network.txt").anyOptimalRoute("Oval","Southwark");
        //List<String> route = new RouteFinderImpl("network1.txt").anyOptimalRoute("A","Z");
        System.out.println("\n\nOptimal Route:");
        System.out.println(route);
    }
}
