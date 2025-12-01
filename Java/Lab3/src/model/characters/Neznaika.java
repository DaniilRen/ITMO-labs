package model.characters;

import model.abstracted.Person;

public class Neznaika extends Person {
    private boolean isWishingToEat;
    private boolean isWishingToLook;

    public Neznaika() {
        super("Незнайка");
        isWishingToEat = true;
        isWishingToLook = false;
    }

    public boolean isWishingToEat() {
        return isWishingToEat;
    }

    public void setWishingToEat(boolean wishing) {
        this.isWishingToEat = wishing;
    }

    public void lookAt(Object object) {
        this.isWishingToLook = true;
        System.out.println(name + " смотрел на " + object.toString());
    }

    public void see(String message) {
        System.out.println(name + " увидел что " + message);
    }

    public void couldStopLooking() {
        if (isWishingToLook) {
            System.out.println("не мог оторваться"); 
        }
        System.out.println("мог оторваться"); 
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