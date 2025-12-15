package model.characters;

import model.abstracted.Character;
import model.abstracted.enums.Feeling;
import model.abstracted.enums.TimeUnit;
import model.abstracted.enums.Wish;
import model.abstracted.interfaces.Lookable;
import model.abstracted.interfaces.Location;
import model.objects.Planet;
import model.objects.Rocket;

public class Neznaika extends Character {
    private Wish wish;

    public Neznaika() {
        super("Незнайка");
    }

    @Override
    protected void describeLocation() {
        System.out.println(this.name + " спустился в " + this.location);
    }

    @Override
    public void setLocation(Location location) {
        super.setLocation(location);
        if (location instanceof Rocket rocket) {
            this.describeRocketSpeed(rocket);
            this.describeRocketApproaching(rocket);
        } else {
            this.describeLocation();
        }
    }

    private void describeRocketSpeed(Rocket rocket) {
        Feeling speedFeeling;
        if (rocket.getSpeed().distance().toInteger() > 10 && rocket.getSpeed().timeUnit() == TimeUnit.SECOND) {
            speedFeeling = Feeling.SCARY;
        } else {
            speedFeeling = Feeling.CALMNESS;
        }
        System.out.println(rocket.getName() + " мчалась со " + speedFeeling.toAdjective() + " скоростью " + rocket.getSpeed().toString());
    }

    private void describeRocketApproaching(Rocket rocket) {
        String ApproachingMeasure;
        if (rocket.getPath().distance().toInteger() / rocket.getSpeed().distance().toInteger() < 1000) {
            ApproachingMeasure = "быстро ";
        } else {
            ApproachingMeasure = "застыла на месте и ни на пол пальца не ";
        }
        System.out.println(this.getName() + " думал, что она " + ApproachingMeasure + "приближается к " + rocket.getPath().endPoint().name());
    }

    public void setWish(Wish wish) {
        this.wish = wish;
        System.out.println(this.getName() + " почувстовал " + this.wish.toFeeling().toNoun());
        System.out.println(this.getName() + " сообразил, что настало время " + this.wish);
    }

    public Wish getWish() {
        return this.wish;
    }

    private String couldStopObserving() {
        if (this.observedObject instanceof Planet planet &&  planet.name().equals("Луна")) {
            return "не мог оторваться\n" + planet.attractViews(); 
        }
        return "вполне мог перестать"; 
    }


    @Override
    public void setObservedObject(Lookable observedObject) {
        this.observedObject = observedObject;
        this.describeObserve();
    }

    private void describeObserve() {
        if (this.observedObject instanceof Ponchik ponchik) {
            System.out.println(this.getName() + " увидел, что " + ponchik.describeSleeping() + " и " + ponchik.describeEating());
        } else{ 
            System.out.println(this.getName() + " смотрел на " + this.observedObject + " и " + this.couldStopObserving());
        }

    }
}