package model.objects;

import model.abstracted.Person;
import model.abstracted.interfaces.Location;
import model.abstracted.interfaces.Lookable;


public record Planet(String name) implements Location, Lookable {
    public void attract(Person person) {
        System.out.println(this.name + " притягивала к себе взоры " + person.getName());
    }

    public String attractViews() {
        return this.name + " словно притягивала к себе взоры";
    }

    @Override
    public String toString() {
        return this.name;
    }
}