package bg.sofia.uni.fmi.mjt.itinerary.graph;

import bg.sofia.uni.fmi.mjt.itinerary.City;
import bg.sofia.uni.fmi.mjt.itinerary.Journey;
import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;
import bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SequencedCollection;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Collections;

public class Graph {
    private static final BigDecimal HEURISTICS = BigDecimal.valueOf(20);
    private static final BigDecimal DIV = BigDecimal.valueOf(1000); //m->km
    Map<Node, List<Edge>> graph;
    Node start;
    Node end;

    public Graph(List<Journey> journeys, City start, City end) {
        Map<City, List<Journey>> buffer = new HashMap<>(); //initial buffer
        for (Journey journey:journeys) {

            buffer.putIfAbsent(journey.from(), new LinkedList<>());
            buffer.putIfAbsent(journey.to(), new LinkedList<>());

            buffer.get(journey.from()).add(journey);
        }

        Map<City, Node> legend = new HashMap<>();
        for (City key: buffer.keySet()) {
            legend.put(key, new Node(key));
        }

        this.start = legend.get(start);
        this.end = legend.get(end);
        graph = new HashMap<>();
        for (City key: buffer.keySet()) {
            Node current = legend.get(key);
            graph.put(current, new LinkedList<>());

            if (buffer.get(key).isEmpty()) { //to avoid the exception in the for loop below
                continue;
            }

            for (Journey journey: buffer.get(key)) {
                Node to = legend.get(journey.to());
                BigDecimal price = journey.price();
                VehicleType type = journey.vehicleType(); //for readability

                //System.out.println("graph1");
                graph.get(current).add(new Edge(current, to, price, type));
            }

        }
    }

    private BigDecimal calculateH(Node o) { // |x1-x2| + |y1-y2| :  x2,y2 - end Node
        BigDecimal calculate = BigDecimal.valueOf(
                (Math.abs(o.getCity().location().x() - end.getCity().location().x()) +
                Math.abs(o.getCity().location().y() - end.getCity().location().y())));

        calculate = calculate.divide(DIV);
        calculate = HEURISTICS.multiply(calculate);

        o.setHCost(calculate);
        return calculate;
    }

    private SequencedCollection<Journey> trackPath(Node node) {
        List<Journey> retList = new LinkedList<>();
        Node iteration = node;

        while (iteration.parent != null) {
            VehicleType type = iteration.parent.getType();
            City from = iteration.parent.fromCity.getCity();
            City to = iteration.getCity();
            BigDecimal price = iteration.parent.getPrice();
            retList.add(new Journey(type, from, to, price));

            iteration = iteration.parent.fromCity;
        }

        Collections.reverse(retList); //end to start -> start to end
        return retList;
    }

    public SequencedCollection<Journey> findCheapestPath(boolean allowTransfer)
            throws CityNotKnownException, NoPathToDestinationException {

        if (!graph.containsKey(start) || !graph.containsKey(end)) {
            throw new CityNotKnownException("No such city or cities.(findCheapestPath in class Graph)");
        }

        if (!allowTransfer) {
            return noTransfersAllowed();
        } else {
            return trackPath(aStarAlgorithm());
        }
    }

    private Node aStarAlgorithm() throws NoPathToDestinationException {
        Queue<Node> closed = new PriorityQueue<>();
        Queue<Node> opened = new PriorityQueue<>();
        start.setAllCost(BigDecimal.valueOf(0), calculateH(start));
        opened.add(start);
        while (!opened.isEmpty()) {
            Node current = opened.peek();
            if (current.equals(end)) {
                return current;
            }
            for (Edge edge: graph.get(current)) {
                Node neighbour = edge.getNode(); //we get the toCity
                BigDecimal weight = edge.getFullPrice().add(current.getgCost());
                if (!opened.contains(neighbour) && !closed.contains(neighbour)) {
                    setNodeUp(neighbour, edge, weight);
                    opened.add(neighbour);
                } else {
                    if (weight.compareTo(neighbour.getgCost()) < 0) {
                        setNodeUp(neighbour, edge, weight);
                        if (closed.contains(neighbour)) {
                            swapNode(neighbour, closed, opened);
                        }
                    }
                }
            }
            swapNode(current, opened, closed);
        }
        throw new NoPathToDestinationException("There is no path(astar in graph)");
    }

    private void setNodeUp(Node n, Edge e, BigDecimal weight) {
        n.parent = e;
        n.setAllCost(weight, calculateH(n));
    }

    private void swapNode(Node n, Queue o1, Queue o2) {
        o1.remove(n);
        o2.add(n);
    }

    private SequencedCollection<Journey> noTransfersAllowed() throws NoPathToDestinationException {
        Edge lowestPrice = null;
        List<Edge> directFlights = graph.get(start);
        if (directFlights.isEmpty()) { //avoiding the exception in the for loop below
            throw new NoPathToDestinationException("No travels from starting point");
        }

        boolean checkForException = true;
        for (Edge travel: directFlights) {
            if (travel.getNode() == end) {
                checkForException = false;
                lowestPrice = travel;
                break;
            }
        }

        if (checkForException) {
            throw new NoPathToDestinationException("No direct flights to ending point");
        }

        for (Edge travel: directFlights) {
            Node current = travel.getNode();
            if (current == end && travel.getFullPrice().compareTo(lowestPrice.getFullPrice()) < 0) {
                lowestPrice = travel;
            }
        }
        List<Journey> ret = new LinkedList<>();
        ret.add(new Journey(lowestPrice.getType(), start.getCity(), end.getCity(), lowestPrice.getPrice()));
        return ret;
    }
}
