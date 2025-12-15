package model.objects;

import model.abstracted.Character;
import model.abstracted.interfaces.Location;
import model.abstracted.interfaces.Lookable;


public record Planet(String name) implements Location, Lookable {
    public void attract(Character character) {
        System.out.println(this.name + " притягивала к себе взоры " + character.getName());
    }

    public String attractViews() {
        return this.name + " словно притягивала к себе взоры";
    }

    @Override
    public String toString() {
        return this.name;
    }
}