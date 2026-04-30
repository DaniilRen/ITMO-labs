package common.models;

import java.io.Serializable;
import java.util.Comparator;


/**
 * Абстрактный класс элементов коллекции.
 * @author Septyq
 */
public abstract class Entity implements Validatable, Comparable<Entity>, Serializable {
    protected int id;

    public int getId() {
        return id;
    };

    public void setId(Integer id) {
        this.id = id;
    };

    public void update(Entity newEntity) {}
    
    protected Comparator<Entity> getComparator() {
        return Comparator.comparingInt(Entity::getId);
    }
    public int compareTo(Entity other) {
        return getComparator().compare(this, other);
    }
}
