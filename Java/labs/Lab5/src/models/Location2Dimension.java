package models;

import java.util.Objects;
import util.Validatable;

public class Location2Dimension implements Validatable {
    private final Integer x;
    private final double y;
    private String name;

    public Location2Dimension(Integer x, double y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    @Override
    public boolean validate() {
        return x != null && !(name == null || name.isEmpty());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location2Dimension convertedObject = (Location2Dimension) o;
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
}
