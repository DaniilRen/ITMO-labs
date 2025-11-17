package model.objects;

import model.abstracted.Character;
import model.abstracted.Distance;
import model.abstracted.Measure;

public class Rocket implements Character {
    private final String name;
    private final Distance speed;

    public Rocket(String name, Distance speed) {
        this.name = name;
        this.speed = speed; 
    }

    public String getSpeed() {
        return speed.toString()+" в одну секунду";
    }

    public String coverDistance() {
        return " покрывая пространство в " + this.getSpeed();
    }

    public void rush(Measure measure) {
        System.out.println(name + " мчалась со " + measure.toString() + " скоростью " + this.coverDistance());
    }

    public String freeze() {
        return name + " застыла на месте";
    }

    public String approaching(Planet planet, Measure measure) {
        return measure.toString() + " приближается к " + planet.name(); 
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}