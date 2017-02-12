package com.byhiras.graph.optimized;

import com.byhiras.graph.optimized.model.Platform;
import com.byhiras.graph.optimized.model.Station;
import com.byhiras.graph.optimized.model.Route;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.*;

public class RouteFinderImpl implements RouteFinder {

    private Map<String, List<Route>> visitedRoutes = new HashMap<String, List<Route>>();

    private Map<String, Station> stations = new HashMap<String, Station>();

    public RouteFinderImpl(String tubeNetworkDescriptionFilePath){
        String map = loadFile(tubeNetworkDescriptionFilePath);
        for(String textline : map.split("\n")) {
            String[] nodes = textline.split("\\^");

            // One entry means only the TubeLine name is present which by itself is not of much use
            if(nodes.length <= 1) {
                continue;
            }
            String line = nodes[0];
            List<String> tubeStations = Arrays.asList(Arrays.copyOfRange(nodes, 1, nodes.length));
            Station previousStation = null;

            for(String tubeStation : tubeStations) {
                Station currrentStation = stations.getOrDefault(tubeStation, new Station(tubeStation));
                currrentStation.addAdjacentStation(previousStation);
                currrentStation.addPlatform(new Platform(line, currrentStation));

                if(previousStation != null)
                    previousStation.addAdjacentStation(currrentStation);

                stations.put(tubeStation, currrentStation);
                previousStation = currrentStation;
            }
        }
    }

    public List<String> anyOptimalRoute(String originStation, String destinationStation){

        // Reversing as it is a depth first approach
        Station origin = stations.get(destinationStation);
        Station destination = stations.get(originStation);

        if(origin == null)
            throw new RuntimeException(originStation + " doesn't exist");
        if(destination == null)
            throw new RuntimeException(destinationStation + " doesn't exist");

        List<Route> allRoutes = anyOptimalRouteForStation(origin, destination, new HashSet<Station>());

        if(allRoutes.size() == 0)
            throw new RuntimeException("No path exists from " + originStation + " to " + destinationStation);

        Route minCostRoute = allRoutes.get(0);
        for(int i=0; i<allRoutes.size(); i++){
            System.out.println(allRoutes.get(i).returnStationList() + " [" + allRoutes.get(i).calculateCost() + "]");
            if(minCostRoute.calculateCost() > allRoutes.get(i).calculateCost())
                minCostRoute = allRoutes.get(i);
        }
        return minCostRoute.returnStationList();
    }
    public List<Route> anyOptimalRouteForStation(Station origin, Station destination, Set<Station> visitedStations) {

        // [Base Case] Source == Destination
        List<Route> routes = new ArrayList<Route>();
        if (origin.equals(destination)) {
            Route route = new Route(new ArrayList<Station>(Arrays.asList(destination)));
            return new ArrayList<Route>(Arrays.asList(route));
        }
        // Mark as visited
        visitedStations.add(origin);

        // If no unvisted adjacent nodes
        List<Station> adjacentStations = origin.getAdjacentStations(visitedStations);
        if(adjacentStations.size() == 0)
            return routes;

        // Going through adjacent nodes
        for(Station via : adjacentStations){

            // This key takes a snapshot in time so if we see the same scenario again
            // we don't have to do all the work again. The important bit here is the
            // visited stations, which helps in the cases when no path is found
            String visitedKey = getKey(origin, via, destination, visitedStations);

            Set<Station> currentVisitedStations = new HashSet<Station>(visitedStations);
            List<Route> returnedRoutes = new ArrayList<Route>();

            // Memoization
            if(visitedRoutes.containsKey(visitedKey)) {
                returnedRoutes = visitedRoutes.get(visitedKey);
            }

            // Recursion by going via adjacent nodes
            else {
                List<Route> returnedRoutesTemp = anyOptimalRouteForStation(via, destination, currentVisitedStations);
                for(Route returnedRouteTemp : returnedRoutesTemp)
                    returnedRoutes.add(new Route(returnedRouteTemp.getStations(), origin));

                // Add the current node to the visited path so you don't
                // have to do the same work again
                visitedRoutes.put(visitedKey, returnedRoutes);
            }

            // All routes from the current station
            for(Route returnedRoute : returnedRoutes)
                routes.add(new Route(returnedRoute.getStations()));
        }
        return routes;
    }

    private String getKey(Station origin, Station via, Station destination, Set<Station> visitedStations){
        return String.format("%s-%s-%s-%s", origin.getName(), via.getName(), destination.getName(), visitedStations.hashCode());
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
