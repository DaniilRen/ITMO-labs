package model.objects;

import model.abstracted.Location;
import model.abstracted.Lookable;
import model.abstracted.Person;


public record Planet(String name) implements Location, Lookable {
    public void attract(Person person) {
        System.out.println(this.name + " притягивала к себе взоры " + person.getName());
    }
}