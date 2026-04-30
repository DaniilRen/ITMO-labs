package common.models;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;


/**
 * Класс пути.
 * @author Septyq
 */
public class Route extends Entity {
    private static final long serialVersionUID = 19388910L;

    private int id;
    private String name;
    private Coordinates coordinates;
    private LocalDateTime creationDate;
    private Location2Dimension from;
    private Location3Dimension to;
    private int distance;
    private String author;

    public Route(String name, Coordinates coordinates, LocalDateTime creationDate, 
            Location2Dimension from, Location3Dimension to, int distance, String author) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.author = author;
    }

    public Route(int id, String name, Coordinates coordinates, LocalDateTime creationDate, 
            Location2Dimension from, Location3Dimension to, int distance, String author) {
        this(name, coordinates, creationDate, from, to, distance, author);
        this.id = id;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Location2Dimension getLocationFrom() {
        return from;
    }

    public Location3Dimension getLocationTo() {
        return to;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDistance() {
        return distance;
    }
    public String getAuthor() {
        return author;
    }

    @Override
    public boolean validate() {
        return distance > 1
            && name != null && !(name.isEmpty())
            && creationDate != null
            && coordinates.validate()
            && from.validate()
            && to.validate()
            && author != null;
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
            && Objects.equals(to, convertedObject.to)
            && Objects.equals(author, convertedObject.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, from, to);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("id = %d", id) + "\n");
        result.append(String.format("creation date = %s", creationDate) + "\n");
        result.append(String.format("name = %s", name) + "\n");
        result.append(String.format("coordinates = %s", coordinates) + "\n");
        result.append(String.format("from = %s", from) + "\n");
        result.append(String.format("to = %s", to) + "\n");
        result.append(String.format("distance = %d", distance) + "\n");
        result.append(String.format("author = %s", author) + "\n");
        return result.toString();
    }

    @Override
    protected Comparator<Entity> getComparator() {
        return Comparator.comparing((Entity e) -> ((Route)e).getName());
    }
}
