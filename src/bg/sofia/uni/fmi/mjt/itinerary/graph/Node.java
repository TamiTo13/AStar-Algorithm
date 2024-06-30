package bg.sofia.uni.fmi.mjt.itinerary.graph;

import bg.sofia.uni.fmi.mjt.itinerary.City;

import java.math.BigDecimal;

public class Node implements Comparable<Node> {

    private final City city;
    public Edge parent;
    private BigDecimal gCost; // distance from starting city
    private BigDecimal hCost; //distance to destination city
    private BigDecimal fCost; //f = g + h

    public Node(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    public void setGCost(BigDecimal gCost) {
        this.gCost = gCost;
    }

    public void setHCost(BigDecimal hCost) {
        this.hCost = hCost;
    }

    public void calculateF() {
        if (hCost == null) { //because h() is not calculated and we cant get f
            throw new CantCalculateFException();
        }

        this.fCost = gCost.add(hCost);
    }

    public void setAllCost(BigDecimal gCost, BigDecimal hCost) {
        setGCost(gCost);
        setHCost(hCost);
        this.fCost = gCost.add(hCost);
    }

    public BigDecimal getgCost() {
        return this.gCost;
    }

    @Override
    public int compareTo(Node o) {
        if (this.fCost.compareTo(o.fCost) == 0) {
            if (this.hCost.compareTo(o.hCost) != 0) {
                return o.hCost.compareTo(this.hCost); //Because we want the smaller one
            } else {
                return this.city.name().compareTo(o.city.name());
            }
        } else {
            return this.fCost.compareTo(o.fCost);
        }
    }

    @Override //so we can access the node directly through the city's hashcode
    public int hashCode() {
        return this.city.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Node || o instanceof City) {
            return this.hashCode() == o.hashCode();
        }
        return false;
    }
}

