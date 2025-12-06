package model.objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.abstracted.Character;
import model.abstracted.Feeling;
import model.abstracted.Lookable;
import model.abstracted.Speed;
import model.abstracted.TimeUnit;
import model.exceptions.CannotDescribeSpeedException;
import model.exceptions.NotUniqueCompartments;
import model.abstracted.Path;

public class Rocket implements Character, Lookable {
    private final ArrayList<Compartment> compartments;
    private final String name;
    private Speed speed;
    private Feeling speedFeeling;
    private Path path;
    private boolean isRushing;

    public Rocket(String name, ArrayList<Compartment> compartments) throws NotUniqueCompartments {
        this.name = name;
        this.checkUniqueCompartments(compartments);
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
        this.setSpeedFeeling();;
    }

    public void setSpeedFeeling() {
        if (this.getSpeed().distance().toInteger() > 10 && this.getSpeed().timeUnit() == TimeUnit.SECOND) {
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

    public ArrayList<Compartment> getCompartments() {
        return this.compartments;
    }

    public void checkUniqueCompartments(List<Compartment> compartments) 
            throws NotUniqueCompartments {
        Set<String> types = new HashSet<>();
        for (Compartment c : compartments) {
            if (!types.add(c.type())) {
                throw new NotUniqueCompartments("compartments shall have unique 'type' field; got duplicate field: " + c.type());
            }
        }
    }

    public void describeRush() {
        System.out.println(name + " мчалась со " + this.speedFeeling.toAdjective() + " скоростью " + this.speed.toString());
    }

    public String describeApproaching() throws CannotDescribeSpeedException {
        if (this.getSpeed() == null || this.getPath() == null) {
            throw new CannotDescribeSpeedException("you need not set rocket speed and path to get approaching description!");
        }

        String ApproachingMeasure;
        if (this.getPath().distance().toInteger() / this.getSpeed().distance().toInteger() < 1000) {
            ApproachingMeasure = "быстро ";
        } else {
            ApproachingMeasure = "ни на пол пальца не ";
        }
        return this.name + " " + ApproachingMeasure + "приближается к " + this.path.endPoint().name();
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