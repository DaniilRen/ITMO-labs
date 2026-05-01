package common.models;

import java.util.Objects;


/**
 * Представление начальной локации в коллекции
 * @author Septyq
 */
public class LocationFrom extends Entity {
    private static final long serialVersionUID = 45462112L;

    private final Integer x;
    private final Double y;
    private String name;

    public LocationFrom(Integer x, Double y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    @Override
    public boolean validate() {
        return x != null && !(name.isEmpty());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationFrom convertedObject = (LocationFrom) o;
        return Objects.equals(x, convertedObject.x) 
            && Objects.equals(y, convertedObject.y)
            && Objects.equals(name, convertedObject.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, name);
    }

    @Override
    public String toString() {
        return String.format("(x=%d, y=%f, name=%s)", x, y, name);
    }

    public int getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getName() {
        return name;
    }
}
