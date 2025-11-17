package model.objects;

import model.abstracted.Person;


public record Planet(String name) {
    public void attract(Person person) {
        System.out.println(this.name + " притягивала к себе взоры " + person.getName());
    }
}