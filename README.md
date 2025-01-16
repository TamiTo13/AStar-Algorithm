# RideRight :busstop:

This is a solution to a travel itinerary planner problem that finds the cheapest path between two cities, possibly with transfers, using various transport types (bus, train, and plane). The problem requires the application of the A* algorithm and the use of Manhattan distance as the heuristic.

## Task Description

In this task, I was tasked with developing an application to plan travel between two cities, with possible transfers, using different vehicle types like planes, trains, and buses. The application finds the most cost-effective route while considering the pollution tax associated with each transport type.

### Features:

- **Vehicle Types**: The vehicle types are planes, trains, and buses. Each has an associated environmental tax:
  - **Plane**: 25% of the price
  - **Train**: 0% (no environmental tax)
  - **Bus**: 10% of the price
- **Cities and Locations**: Cities are defined by their name and location (on a 2D plane).
- **Manhattan Distance**: The heuristic used in the A* algorithm is the Manhattan distance, considering that travel is constrained to axis-aligned paths.

### Problem Details

The world is a flat plane, with cities having coordinates on the 2D plane. Each journey between two cities is a direct connection and has a given price. The objective is to find the cheapest path from one city to another, potentially involving transfers.

The input is a list of direct journeys (using different vehicle types), and the task is to find the cheapest path while considering the pollution tax, as well as using A* search with Manhattan distance as the heuristic.

### Project Structure

The project implements the following core components:

- **City**: Represents a city with a name and location on the 2D plane.
- **Journey**: Represents a direct trip between two cities using a specific vehicle type (plane, bus, or train) and its price.
- **RideRight**: The main class responsible for finding the cheapest travel route. It implements the `ItineraryPlanner` interface and uses A* to find the optimal path.
- **VehicleType**: Enum that defines the vehicle types (Plane, Train, Bus) along with their environmental tax rates.

### Algorithm Used

For finding the cheapest path, the project uses the **A\* algorithm** with the **Manhattan distance** heuristic. This ensures that the algorithm efficiently calculates the cheapest route considering both price and environmental impact (via pollution tax).

### Example Input

```java
City sofia = new City("Sofia", new Location(0, 2000));
City plovdiv = new City("Plovdiv", new Location(4000, 1000));
City varna = new City("Varna", new Location(9000, 3000));
City burgas = new City("Burgas", new Location(9000, 1000));
City ruse = new City("Ruse", new Location(7000, 4000));
City blagoevgrad = new City("Blagoevgrad", new Location(0, 1000));
City kardzhali = new City("Kardzhali", new Location(3000, 0));
City tarnovo = new City("Tarnovo", new Location(5000, 3000));

List<Journey> schedule = List.of(
    new Journey(BUS, sofia, blagoevgrad, new BigDecimal("20")),
    new Journey(BUS, blagoevgrad, sofia, new BigDecimal("20")),
    new Journey(BUS, sofia, plovdiv, new BigDecimal("90")),
    // Add more journeys as needed...
);

RideRight rideRight = new RideRight(schedule);
