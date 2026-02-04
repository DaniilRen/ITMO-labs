package model.characters;

import model.abstracted.Character;
import model.abstracted.enums.TimeUnit;
import model.abstracted.enums.SpeedDescription;
import model.abstracted.enums.Wish;
import model.abstracted.interfaces.Lookable;
import model.exceptions.EmptyParamsException;
import model.abstracted.interfaces.Location;
import model.objects.Compartment;
import model.objects.Planet;
import model.objects.Rocket;
import model.abstracted.enums.Hour;

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
        if (rocket.getSpeed() == null || rocket.getPath() == null) {
            throw new EmptyParamsException("you should specify rocket`s path and speed");
        }
        SpeedDescription speedDescription;
        if (rocket.getSpeed().distance().toInteger() > 10 && rocket.getSpeed().timeUnit() == TimeUnit.SECOND) {
            speedDescription = SpeedDescription.SCARY;
        } else {
            speedDescription = SpeedDescription.CALM;
        }
        System.out.println(rocket.getName() + " мчалась со " + speedDescription.toString() + " скоростью " + rocket.getSpeed().toString());
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
        System.out.println(this.getName() + " почувстовал " + this.wish.toFeeling().toString());
        System.out.println(this.getName() + " сообразил, что настало время " + this.wish);
        if (this.getWish().equals(Wish.EAT)) {
            this.setLocation(new Compartment("пищевой"));
        } else if (this.getWish().equals(Wish.SLEEP)) {
            this.setLocation(new Compartment("спальный"));
        }
        else if (this.getWish().equals(Wish.REST)) {
            this.setLocation(new Compartment("прогулочный"));
        }
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
        if (this.observedObject instanceof Planet && this.getLocation() instanceof Rocket) {
            System.out.println("Прошло " + Hour.TWO.toString() + " или " + Hour.THREE.toString());
        }
        this.describeObserve();
    }

    private void describeObserve() {
        if (this.observedObject instanceof Ponchik ponchik) {
            System.out.println(this.getName() + " увидел, что " + ponchik.describeState());
        } else{ 
            System.out.println(this.getName() + " смотрел на " + this.observedObject + " и " + this.couldStopObserving());
        }

    }
}