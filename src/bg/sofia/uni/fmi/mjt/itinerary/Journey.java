package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType;

import java.math.BigDecimal;

public record Journey(VehicleType vehicleType, City from, City to, BigDecimal price) {

    public Journey {
        if (vehicleType == null || from == null || to == null || price == null) {
            throw new IllegalArgumentException();
        }
    }

}
