package model.abstracted;

import model.abstracted.enums.State;
import model.abstracted.interfaces.Location;
import model.abstracted.interfaces.Lookable;

public abstract class Character implements Lookable {
    protected String name;
    protected State state;
    protected Location location;
    protected Lookable observedObject;


    public Character(String name) {
        this.name = name;
    }

    protected abstract void describeLocation();


    public void setLocation(Location location) {
        this.location = location;
    }

    public void setObservedObject(Lookable observedObject) {
        this.observedObject = observedObject;
    }

    public void setState(State state) {
        this.state = state;
    }


    public String getName() {
        return name;
    }

    public Location getLocation() {
        return this.location;
    };

    public State getState() {
        return this.state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return name.equals(character.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}