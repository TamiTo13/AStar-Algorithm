package bg.sofia.uni.fmi.mjt.itinerary.graph;

import bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType;

import java.math.BigDecimal;

public class Edge {
    public Node fromCity;
    private Node toCity;
    private BigDecimal fullPrice;
    private BigDecimal price;
    private VehicleType type;

    public Edge(Node fromCity, Node toCity, BigDecimal price, VehicleType type) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.fullPrice = price.add(price.multiply(type.getGreenTax())); //fullprice = price + price*type
        this.price = price;
        this.type = type;
    }

    public Node getNode() {
        return toCity;
    }

    public BigDecimal getFullPrice() {
        return fullPrice;
    }

    public VehicleType getType() {
        return type;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
