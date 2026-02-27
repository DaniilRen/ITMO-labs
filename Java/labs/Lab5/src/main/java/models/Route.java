package models;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

import managers.CollectionManager;

public class Route extends Entity {
    private static int nextId = 1;

    private final int id;
    private String name;
    private Coordinates coordinates;
    private LocalDateTime creationDate;
    private Location2Dimension from;
    private Location3Dimension to;
    private int distance;

    public Route(String name, Coordinates coordinates, LocalDateTime creationDate, 
            Location2Dimension from, Location3Dimension to, int distance) {
        this.id = nextId;
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
    
    public static void updateNextId(CollectionManager collectionManager) {
        var maxId = collectionManager
            .getCollection()
            .stream().filter(Objects::nonNull)
            .map(Entity::getId)
            .mapToInt(Integer::intValue).max().orElse(0);
        nextId = maxId + 1;
    }

    public static void IncNextId() {
        nextId ++;
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
        StringBuilder result = new StringBuilder();
        result.append(String.format("creation date=%s", creationDate) + "\n\n");
        result.append(String.format("name=%s", name) + "\n\n");
        result.append(String.format("coordinates=%s", coordinates) + "\n\n");
        result.append(String.format("from=%s", from) + "\n\n");
        result.append(String.format("to=%s", to) + "\n\n");
        result.append(String.format("distance=%d", distance) + "\n\n");
        return result.toString();
    }

    @Override
    protected Comparator<Entity> getComparator() {
        return Comparator.comparing((Entity e) -> ((Route)e).getName());
    }
}
