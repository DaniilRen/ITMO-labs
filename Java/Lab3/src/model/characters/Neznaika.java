package model.characters;

import model.abstracted.Person;
import model.abstracted.enums.Wish;
import model.abstracted.interfaces.Lookable;
import model.objects.Planet;

public class Neznaika extends Person {
    private Wish wish;
    private String noticed;

    public Neznaika() {
        super("Незнайка");
    }

    public void setWish(Wish wish) {
        this.wish = wish;
    }

    public void setNoticed(String noticed) {
        this.noticed = noticed;
    }

    public String couldStopLooking(Lookable object) {
        if (object instanceof Planet planet &&  planet.name().equals("Луна")) {
            return "не мог оторваться\n" + planet.attractViews(); 
        }
        return "вполне мог перестать"; 
    }

    public String isTimeToWish() {
        if (this.wish == null) {
            return "пока можно ничего не делать";
        } else {
            return "настало время " + this.wish;
        }
    }

    public void describeNotice() {
        System.out.println(this.name + " увидел что " + this.noticed);
    }

    @Override
    public void describeObserve() {
        System.out.println(this.name + " смотрел на " + this.observedObject + " и " + this.couldStopLooking(this.observedObject));
    }

    @Override
    public void describeFeeling() {
        if (this.feeling == null) {
            System.out.println(this.name + " ничего не почувствовал");
        } else {
            System.out.println(this.name + " почуствовал " + this.feeling);
        }
    }

    @Override
    public void describeThoughts() {
        if (this.thoughts == null) {
            System.out.println(this.name + " ничего не думал");
        } else {
            System.out.println(this.name + " думал что " + this.thoughts);
        }
    }

    @Override
    public void describeRealisation() {
        if (this.realisation == null) {
            System.out.println(this.name + " ничего не сообразил");
        } else {
            System.out.println(this.name + " сообразил что " + this.realisation);
        }
    }

    @Override
    public void describeLocation(String describePrefix) {
        if (this.location == null) {
            System.out.println(this.name + " забыл, где находится");
        } else {
            String prefix = describePrefix==null ? " находится в " : describePrefix;
            System.out.println(this.name + prefix + this.location);
        }
    }
}