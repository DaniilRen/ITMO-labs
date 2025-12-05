package model.objects;

import model.abstracted.Location;
import model.abstracted.Lookable;
import model.abstracted.Person;


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