package bg.sofia.uni.fmi.mjt.itinerary;

public record City(String name, Location location) {
    public City {
        if (name == null || location == null) {
            throw new IllegalArgumentException();
        }
    }
}
