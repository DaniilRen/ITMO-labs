package model.characters;

import model.abstracted.Eatable;
import model.abstracted.Person;
import model.abstracted.State;


public class Ponchik extends Person {
    private Eatable chewingObject;

    public Ponchik() {
        super("Пончик");
    }

    @Override
    public void describeObserve() {
        System.out.println(name + " смотрел на " + this.observedObject);
    }

    @Override
    public void describeFeeling() {
        if (this.feeling == null) {
            System.out.println(this.name + " ничего не почувствовал");
        } else {
            System.out.println(name + " почуствовал " + this.feeling);
        }
    }

    @Override
    public void describeThoughts() {
        if (this.thoughts == null) {
            System.out.println(this.name + " ничего не думал");
        } else {
            System.out.println(this.name + " думал что " + this.thoughts);
        }
    }

    @Override
    public void describeRealisation() {
        if (this.realisation == null) {
            System.out.println(this.name + " ничего не сообразил");
        } else {
            System.out.println(this.name + " сообразил что " + this.realisation);
        }
    }

    @Override
    public void describeLocation(String describePrefix) {
        if (this.location == null) {
            System.out.println(this.name + " забыл, где находится");
        } else {
            System.out.println(this.name + describePrefix==null ? " находится в " : describePrefix + this.location);
        }
    }

    public String describeSleeping() {
        if (this.states.contains(State.SLEEPING)) {
            return name + " еще не проснулся";
        }
        return name + " уже проснулся";
    }

    public String describeEating() {
        if (this.states.contains(State.EATING)) {
            if (this.chewingObject == null) {
                return this.name + " " + State.EATING + " что-то";
            }
            return this.name + " " + State.EATING + " " + this.chewingObject;
        }
        return this.name + " ничего не ест";
    }

    public void setChewingObject(Eatable object) {
        this.chewingObject = object;
    }
}
