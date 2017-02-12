package com.byhiras.recruitment.test9;

import com.byhiras.recruitment.test9.model.Route;
import com.byhiras.recruitment.test9.model.Stations;
import com.byhiras.recruitment.test9.model.TubeLines;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.util.*;

public class RouteFinderImpl implements RouteFinder {

    private TubeLines tubeLines = new TubeLines();
    private Stations stations = new Stations();

    public RouteFinderImpl(String tubeNetworkDescriptionFilePath){
        String map = loadFile(tubeNetworkDescriptionFilePath);
        for(String line : map.split("\n")) {
            String[] lineSplit = line.split("\\^");

            // One entry means only the TubeLine name is present which by itself is not of much use
            if(lineSplit.length <= 1) {
                continue;
            }
            String tubeLine = lineSplit[0];
            List<String> tubeStations = Arrays.asList(Arrays.copyOfRange(lineSplit, 1, lineSplit.length));

            tubeLines.add(tubeLine, tubeStations);
            stations.addAll(tubeStations, tubeLine);
        }
        tubeLines.addDirectConnections(stations);
    }

    public List<String> anyOptimalRoute(String originStation, String destinationStation) {

        if(originStation.equals(destinationStation)){
            return Arrays.asList(originStation);
        }

        // We do a prelimary check to see if a path is even possible
        // If the tubeLines for source and destinations intersect
        if(!tubeLines.isPathPossible(
                stations.get(originStation).getInterchanges(),
                stations.get(destinationStation).getInterchanges())){
            System.out.println("Bummer! Path is NOT possible");
            return new ArrayList();
        }

        // Given this is a recursive function, we populate the path in reverse order,
        // hence passing the origin and destination station names in reverse
        List<Route> routes = allRoutes(destinationStation, originStation, new ArrayList());

        int optimalRouteCost = Integer.MAX_VALUE;
        Route optimalRoute = new Route();
        for(Route route : routes){
            System.out.println(String.format("%s [%s]", route.getPath(), route.cost()));
            if(route.cost() < optimalRouteCost) {
                optimalRoute = route;
                optimalRouteCost = route.cost();
            }
        }
        return optimalRoute.getPath();
    }

    // Returns all routes for the source and destination
    // This function is called recursively so 'skip' indicates the
    // stations we have already gone through
    private List<Route> allRoutes(String originStation, String destinationStation, List<String> skip){
        List<Route> routes = new ArrayList();

        Set<String> adjacentStations = stations.get(originStation).getAllAdjacentStations();

        // Remove all stations we have already gone through
        adjacentStations.removeAll(skip);

        // Recusion base condition
        if(adjacentStations.size() == 0)
            return new ArrayList();

        // Huzzah! We have finally reached our destination
        if(adjacentStations.contains(destinationStation)){
            Route route = new Route();
            route.addStation(stations.get(destinationStation));
            route.addStation(stations.get(originStation));
            routes.add(route);
            return routes;
        }

        // Recursive call
        // Going through all adjacent stations as we need to find the best route
        // NOTE: This can be improved by keeping track of your best route so far
        //  and stop the current route if its cost increases in comparision
        //  to the best route
        skip.add(originStation);
        List<String> _skip = new ArrayList(skip);
        for(String adjacentStation : adjacentStations){
            List<Route> tempRoutes = allRoutes(adjacentStation, destinationStation, _skip);
            for(Route route : tempRoutes) {
                route.addStation(stations.get(originStation));
            }
            routes.addAll(tempRoutes);
        }
        return routes;
    }

    private String loadFile(String fileName) {
        String result = "";
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        } catch(NullPointerException e) {
            throw new RuntimeException("Network file could not be loaded");
        }
        return result;
    }
}
