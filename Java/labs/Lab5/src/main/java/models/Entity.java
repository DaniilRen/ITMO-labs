package models;

import java.util.Comparator;

import util.Validatable;

/**
 * Абстрактный класс элементов коллекции.
 * @author Septyq
 */
public abstract class Entity implements Validatable, Comparable<Entity> {
    abstract public int getId();
    public void update(Entity newEntity) {}
    
    protected Comparator<Entity> getComparator() {
        return Comparator.comparingInt(Entity::getId);
    }
    public int compareTo(Entity other) {
        return getComparator().compare(this, other);
    }
}
