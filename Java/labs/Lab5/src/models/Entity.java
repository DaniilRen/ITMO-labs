package models;

import util.Validatable;

public abstract class Entity implements Validatable {
    abstract public int getId();
    public void update(Entity newEntity) {};
}
