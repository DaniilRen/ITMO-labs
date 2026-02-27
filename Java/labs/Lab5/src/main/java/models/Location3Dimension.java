package models;

import java.util.Objects;
import util.Validatable;

public class Location3Dimension implements Validatable{
    private final Double x;
    private final Double y;
    private final Integer z;
    private String name;

    public Location3Dimension(Double x, Double y, Integer z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    @Override
    public boolean validate() {
        return y != null && z != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location3Dimension convertedObject = (Location3Dimension) o;
        return Objects.equals(x, convertedObject.x) 
            && Objects.equals(y, convertedObject.y)
            && Objects.equals(z, convertedObject.z)
            && Objects.equals(name, convertedObject.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, name);
    }

    @Override
    public String toString() {
        return String.format("(x=%f, y=%f, z=%d, name=%s)", x, y, z, name);
    }
}
