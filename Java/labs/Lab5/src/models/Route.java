package models;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

public class Route extends Entity {
    private int currentId = 1;

    private final int id;
    private String name;
    private Coordinates coordinates;
    private LocalDateTime creationDate;
    private Location2Dimension from;
    private Location3Dimension to;
    private int distance;

    public Route(String name, Coordinates coordinates, LocalDateTime creationDate, 
            Location2Dimension from, Location3Dimension to, int distance) {
        this.id = currentId;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public void update(Route newRoute) {
        this.name = newRoute.name;
        this.coordinates = newRoute.coordinates;
        this.creationDate = newRoute.creationDate;
        this.from = newRoute.from;
        this.to = newRoute.to;
        this.distance = newRoute.distance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDistance() {
        return this.distance;
    }

    @Override
    public boolean validate() {
        return id > 0
            && distance > 1
            && name != null && !(name.isEmpty())
            && creationDate != null
            && coordinates.validate()
            && from.validate()
            && to.validate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route convertedObject = (Route) o;
        return id == convertedObject.id 
            && distance == convertedObject.distance
            && Objects.equals(name, convertedObject.name)
            && Objects.equals(coordinates, convertedObject.coordinates) 
            && Objects.equals(creationDate, convertedObject.creationDate)
            && Objects.equals(from, convertedObject.from)
            && Objects.equals(to, convertedObject.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, from, to);
    }

    @Override
    public String toString() {
        String result = "";
        result += String.format("creation date=%s", creationDate);
        result += String.format("name=%s", name);
        result += String.format("coordinates=%s", coordinates);
        result += String.format("from=%s", from);
        result += String.format("to=%s", to);
        result += String.format("distance=%d", distance);
        return result;
    }

    @Override
    protected Comparator<Entity> getComparator() {
        return Comparator.comparing((Entity e) -> ((Route)e).getName());
    }
}
