package model.objects;

import java.util.ArrayList;
import model.abstracted.Character;
import model.abstracted.Feeling;
import model.abstracted.Lookable;
import model.abstracted.Speed;
import model.abstracted.TimeUnit;
import model.abstracted.Path;

public class Rocket implements Character, Lookable {
    private final ArrayList<Compartment> compartments;
    private final String name;
    private Speed speed;
    private Feeling speedFeeling;
    private Path path;
    private boolean isRushing;
    private String ApproachingMeasure;

    public Rocket(String name, ArrayList<Compartment> compartments) {
        this.name = name;
        this.compartments = compartments;
    }

    public void setIsRushing(boolean isRushing) {
        this.isRushing = isRushing;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
        this.setSpeedFeeling();
    }

    public void setSpeedFeeling() {
        if (this.speed.distance().toInteger() > 10 && this.speed.timeUnit() == TimeUnit.SECOND) {
            this.speedFeeling = Feeling.SCARY;
            this.ApproachingMeasure = "ни на пол пальца не";
        } else {
            this.speedFeeling = Feeling.CALMNESS;
            this.ApproachingMeasure = "быстро";
        }
    }


    public boolean getIsRushing() {
        return this.isRushing;
    }

    public Path getPath() {
        return this.path;
    }

    public Speed getSpeed() {
        return speed;
    }

    public ArrayList<Compartment> getCompartments() {
        return this.compartments;
    }

    public void describeRush() {
        System.out.println(name + " мчалась со " + this.speedFeeling.toAdjective() + " скоростью " + this.speed.toString());
    }

    public String describeApproaching() {
        return this.name + " " + this.ApproachingMeasure + "приближается к " + this.path.endPoint().name();
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