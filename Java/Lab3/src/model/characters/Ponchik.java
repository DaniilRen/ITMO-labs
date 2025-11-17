package model.characters;

import model.abstracted.Measure;
import model.abstracted.Person;

public class Ponchik extends Person {
    private boolean isEating;
    private boolean isSleeping;

    public Ponchik() {
        super("Пончик");
        isEating = true;
        isSleeping = false;
    }

    @Override
    public void feal(String message) {
        System.out.println(name + " почуствовал что \"" + message + "\"");
    }

    public void setIsEating(boolean eating) {
        this.isEating = eating;
    }

    public void setIsSleeping(boolean sleeping) {
        this.isSleeping = sleeping;
    }

    public boolean IsEating() {
        return isEating;
    }

    public boolean isSleeping() {
        return isSleeping;
    }

    public String isWokenUp() {
        if (this.isSleeping()) {
            return name + " еще не проснулся";
        }
        return name + " уже проснулся";
    }

    public String eat(Measure measure) {
        return name + " уже что-то жует с " + measure.toString();
    }   
    
}
