package subwaysystem;

import java.util.*;
import java.io.*;

public class SubwaySystem {
    private Map<String, List<StationDistance>> network;

    // Constructor to initialize the subway system from a file
    public SubwaySystem(String filePath) {
        network = new HashMap<>();
        loadSubwayData(filePath);
    }

    // Private method to load subway data from a specified file
    private void loadSubwayData(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length == 3) {
                    String start = parts[0];
                    String end = parts[1];
                    double distance = Double.parseDouble(parts[2]);
                    addConnection(start, end, distance);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read the subway data file: " + e.getMessage());
        }
    }

    // Method to add connections both ways since it's an undirected graph
    private void addConnection(String from, String to, double distance) {
        network.putIfAbsent(from, new ArrayList<>());
        network.putIfAbsent(to, new ArrayList<>());
        network.get(from).add(new StationDistance(to, distance));
        network.get(to).add(new StationDistance(from, distance));
    }

    // Method to find Stations within a specified distance from a starting station
    public List<String> findStationsWithinDistance(String start, double maxDistance) {
        List<String> reachableStations = new ArrayList<>();
        Map<String, Double> distances = new HashMap<>();
        Queue<String> queue = new LinkedList<>();

        queue.add(start);
        distances.put(start, 0.0);

        while (!queue.isEmpty()) {
            String currentStation = queue.poll();
            double currentDistance = distances.get(currentStation);

            for (StationDistance next : network.getOrDefault(currentStation, new ArrayList<>())) {
                double newDistance = currentDistance + next.distance;
                if (newDistance <= maxDistance && (!distances.containsKey(next.station) || newDistance < distances.get(next.station))) {
                    distances.put(next.station, newDistance);
                    queue.add(next.station);
                    if (!reachableStations.contains(next.station)) {
                        reachableStations.add(next.station);
                    }
                }
            }
        }

        return reachableStations;
    }

    // Inner class to represent the station and distance
    private static class StationDistance {
        String station;
        double distance;

        StationDistance(String station, double distance) {
            this.station = station;
            this.distance = distance;
        }
    }

    // Main method to run an example
    public static void main(String[] args) {
        SubwaySystem subway = new SubwaySystem("E:\\eclipse-jee-2023-09-R-win32-x86_64\\sub");
        List<String> stations = subway.findStationsWithinDistance("Central", 5.0);
        System.out.println("Stations within 5 km of 'Central': " + stations);
    }
}
