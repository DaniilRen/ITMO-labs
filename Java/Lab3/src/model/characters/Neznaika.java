package model.characters;

import model.abstracted.Person;
import model.abstracted.Lookable;
import model.objects.Planet;

public class Neznaika extends Person {
    private boolean isWishingToEat;

    public Neznaika() {
        super("Незнайка");
        isWishingToEat = true;
    }

    public boolean isWishingToEat() {
        return isWishingToEat;
    }

    public void setWishingToEat(boolean wishing) {
        this.isWishingToEat = wishing;
    }

    public void lookAt(Lookable object) {
        System.out.println(name + " смотрел на " + object.toString() + " и " + this.couldStopLooking(object));
    }

    public void see(String message) {
        System.out.println(name + " увидел что " + message);
    }

    public String couldStopLooking(Lookable object) {
        if (object instanceof Planet planet &&  planet.name().equals("Луна")) {
            return "не мог оторваться\n" + planet.attractViews(); 
        }
        return "вполне мог перестать"; 
    }

    @Override
    public void describeFeeling() {
        System.out.println(name + " почуствовал " + this.feeling);
    }

    public void think(String message) {
        System.out.println(name + " думал что " + message);
    }

    public void realise(String message) {
        System.out.println(name + " сообразил что " + message);
    }

    public void move_down_to(String message) {
        System.out.println(name + " спустился в " + message);
    }

}