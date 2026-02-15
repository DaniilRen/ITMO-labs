package models;

import java.util.Objects;
import util.Validatable;


public class Coordinates implements Validatable {
    private final Float x;
    private final Long y;

    public Coordinates (Float x, Long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean validate() {
        return x != null && y != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates convertedObject = (Coordinates) o;
        return Objects.equals(x, convertedObject.x) && Objects.equals(y, convertedObject.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("(x=%f, y=%d)", x, y);
    }
}
