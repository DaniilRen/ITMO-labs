package model.abstracted;

import java.util.ArrayList;

import model.abstracted.enums.Feeling;
import model.abstracted.enums.State;
import model.abstracted.interfaces.Character;
import model.abstracted.interfaces.Location;
import model.abstracted.interfaces.Lookable;

public abstract class Person implements Character, Lookable {
    protected String name;
    protected Feeling feeling;
    protected ArrayList<State> states = new ArrayList<State>();
    protected String thoughts; 
    protected String realisation;
    protected Location location;
    protected Lookable observedObject;


    public Person(String name) {
        this.name = name;
    }

    public void setFealing(Feeling feeling) {
        this.feeling = feeling;
    }

    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }

    public void setRealisation(String realisation) {
        this.realisation = realisation;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setObservedObject(Lookable observedObject) {
        this.observedObject = observedObject;
    }

    public void setState(State state) {
        this.states.add(state);
    }

    public void removeState(State state) {
        this.states.remove(state);
    }

    public abstract void describeFeeling();
    public abstract void describeThoughts();
    public abstract void describeRealisation();
    public abstract void describeObserve();
    public abstract void describeLocation(String describePrefix);

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return name.equals(person.name);
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