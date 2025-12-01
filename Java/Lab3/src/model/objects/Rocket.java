package model.objects;

import java.util.ArrayList;
import model.abstracted.Character;
import model.abstracted.Distance;
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
        } else {
            this.speedFeeling = Feeling.CALMNESS;
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

    public void DescribeRush() {
        System.out.println(name + " мчалась со " + this.speedFeeling + " скоростью " + this.speed.toString());
    }

    public String coverDistance() {
        return " покрывая пространство в " + this.getSpeed();
    }


    public String Describefreezing() {
        return name + " застыла на месте";
    }

    public String DescribeApproaching() {
        return name + " приближается к " + this.path.endPoint(); 
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