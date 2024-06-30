package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;
import bg.sofia.uni.fmi.mjt.itinerary.graph.Graph;

import java.util.LinkedList;
import java.util.List;
import java.util.SequencedCollection;

public class RideRight implements ItineraryPlanner {

    List<Journey> schedule;

    public RideRight(List<Journey> schedule) {
        this.schedule = new LinkedList<>();
        this.schedule.addAll(schedule);
    }

    @Override
    public SequencedCollection<Journey> findCheapestPath(City start, City destination, boolean allowTransfer)
            throws CityNotKnownException, NoPathToDestinationException {
        if (start == null || destination == null || schedule == null || start.equals(destination)) {
            throw new IllegalArgumentException();
        }

        Graph solution = new Graph(schedule, start, destination);
        return solution.findCheapestPath(allowTransfer);
    }
}
