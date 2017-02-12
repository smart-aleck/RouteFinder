package com.byhiras.graph.rudimentary;

import com.byhiras.graph.rudimentary.model.Platform;
import com.byhiras.graph.rudimentary.model.Route;
import com.byhiras.graph.rudimentary.model.Station;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.*;

public class RouteFinderImpl implements RouteFinder {

    // Visited
    // Key = "<SourcePlatform>-<AdjacentPlatform>-<DestinationPlatform>-HashOfVistedPlatforms" --> Platform (Station,Line)
    // Value = List of Platforms | null (if not found)
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
            Platform previousPlatform = null;

            for(String tubeStation : tubeStations) {
                Station currrentStation = stations.getOrDefault(tubeStation, new Station(tubeStation));
                Platform currentPlatform = currrentStation.addPlatform(new Platform(line, currrentStation));

                // Setting the Adjacent Platforms
                currentPlatform.addAdjacentPlatform(previousPlatform);
                if(previousPlatform != null)
                    previousPlatform.addAdjacentPlatform(currentPlatform);

                stations.put(tubeStation, currrentStation);
                previousPlatform = currentPlatform;
            }
        }
    }

    public List<String> anyOptimalRoute(String originStation, String destinationStation){
        Station origin = stations.get(destinationStation);
        Station destination = stations.get(originStation);
        if(origin == null)
            throw new RuntimeException(originStation + " doesn't exist");
        if(destination == null)
            throw new RuntimeException(destinationStation + " doesn't exist");

        List<Route> allRoutes = new ArrayList<Route>();
        for(Platform originPlatform : origin.getPlatforms()) {
            for (Platform destinationPlatform : destination.getPlatforms()) {
                List<Route> routes = anyOptimalRouteForPlatform(originPlatform, destinationPlatform, new HashSet<Platform>());
                if (routes.size() > 0)
                    allRoutes.addAll(routes);
            }
        }
        if(allRoutes.size() == 0)
            throw new RuntimeException("No path exists from " + originStation + " to " + destinationStation);

        Route minCostRoute = allRoutes.get(0);
        for(int i=1; i<allRoutes.size(); i++){
            if(minCostRoute.calculateCost() > allRoutes.get(i).calculateCost())
                minCostRoute = allRoutes.get(i);
        }
        return minCostRoute.returnStationList();
    }
    public List<Route> anyOptimalRouteForPlatform(Platform originPlatform, Platform destinationPlatform, Set<Platform> visitedPlatforms) {

        // [Base Case] Source == Destination
        List<Route> routes = new ArrayList<Route>();
        if (originPlatform.equals(destinationPlatform)) {
            Route route = new Route(new ArrayList<Platform>(Arrays.asList(originPlatform)));
            return new ArrayList<Route>(Arrays.asList(route));
        }
        // Mark as visited
        visitedPlatforms.add(originPlatform);

        // If no unvisted adjacent nodes
        List<Platform> adjacentPlatforms = originPlatform.getAdjacentPlatforms(visitedPlatforms);
        if(adjacentPlatforms.size() == 0)
            return routes;

        // Going through adjacent nodes
        for(Platform adjacentPlatform : adjacentPlatforms){

            // This key takes a snapshot in time so if we see the same scenario again
            // we don't have to do all the work again. The important bit here is the
            // visited platforms, which helps in the cases when no path is found
            String visitedKey = getKey(originPlatform, adjacentPlatform, destinationPlatform, visitedPlatforms);
            //System.out.println(visitedKey);

            Set<Platform> currentVisitedPlatforms = new HashSet<Platform>(visitedPlatforms);
            List<Route> returnedRoutes = new ArrayList<Route>();

            // Memoization
            if(visitedRoutes.containsKey(visitedKey)) {
                returnedRoutes = visitedRoutes.get(visitedKey);
                System.out.println(visitedKey + " (" + returnedRoutes.size() + ") [" + visitedPlatforms.hashCode() + "] | Oh yea, saved some time...");
            }

            // Recursion by going via adjacent nodes
            else {
                List<Route> returnedRoutesTemp = anyOptimalRouteForPlatform(adjacentPlatform, destinationPlatform, currentVisitedPlatforms);
                for(Route returnedRouteTemp : returnedRoutesTemp)
                    returnedRoutes.add(new Route(returnedRouteTemp.getPlatforms(), originPlatform));

                // Add the current node to the path so you don't
                // have to do the same work again
                //if(returnedRoutes.size() > 0)
                visitedRoutes.put(visitedKey, returnedRoutes);
            }

            // All routes from the current platform
            for(Route returnedRoute : returnedRoutes)
                routes.add(new Route(returnedRoute.getPlatforms()));
        }
        return routes;
    }

    private String getKey(Platform originPlatform, Platform adjacentPlatform, Platform destinationPlatform, Set<Platform> visitedPlatforms){
        return String.format("%s,%s-%s-%s-%s,%s-%s",
                originPlatform.getStation().getName(), originPlatform.getLine(),
                adjacentPlatform.getStation().getName(), adjacentPlatform.getLine(),
                destinationPlatform.getStation().getName(), destinationPlatform.getLine(),
                //0
                visitedPlatforms.hashCode()
                );
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
