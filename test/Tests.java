import bg.sofia.uni.fmi.mjt.itinerary.City;
import bg.sofia.uni.fmi.mjt.itinerary.Journey;
import bg.sofia.uni.fmi.mjt.itinerary.Location;
import bg.sofia.uni.fmi.mjt.itinerary.RideRight;
import bg.sofia.uni.fmi.mjt.itinerary.graph.Node;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType.*;

public class Tests {

    @Test
    void testCompare() {
        City A = new City("A", new Location(0, 0));
        City B = new City("C", new Location(-1, 1));
        City C = new City("B", new Location(1, 1));
        City D = new City("D", new Location(0, 3));
        City E = new City("E", new Location(2,(int)Math.pow(2,31)));
        City F = new City("F", new Location(123,1231));

        List<Journey> schedule = List.of(
                new Journey(TRAIN, A, B, new BigDecimal("21")),
                new Journey(BUS, A, C, new BigDecimal(Math.pow(3,31))),
                new Journey(BUS, B, D, new BigDecimal("10")),
                new Journey(BUS, C, D, new BigDecimal("10")),
                new Journey(BUS, B, E, new BigDecimal("1")),
                new Journey(BUS, E, D, new BigDecimal("4")),
                new Journey(PLANE, F, A, new BigDecimal("2"))
        );

        RideRight a = new RideRight(schedule);
        try {

            Node o1 = new Node(B);
            Node o2 = new Node(C);
            o1.setAllCost(BigDecimal.valueOf(20), BigDecimal.valueOf(10));
            o2.setAllCost(BigDecimal.valueOf(20), BigDecimal.valueOf(10));

            System.out.println(a.findCheapestPath(A, D, true));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
