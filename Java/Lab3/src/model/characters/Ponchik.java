package model.characters;

import model.abstracted.Eatable;
import model.abstracted.Person;
import model.exceptions.WrongFoodException;
import model.objects.Food;

public class Ponchik extends Person {
    private boolean isEating;
    private boolean isSleeping;
    private Eatable chewingObject;

    public Ponchik() {
        super("Пончик");
        isEating = true;
        isSleeping = false;
    }

    @Override
    public void describeFeeling() {
        System.out.println(name + " почуствовал" + feeling);
    }

    public void setIsEating(boolean eating) {
        isEating = eating;
    }

    public void setIsSleeping(boolean sleeping) {
        isSleeping = sleeping;
    }

    public boolean getIsEating() {
        return isEating;
    }

    public boolean getIsSleeping() {
        return isSleeping;
    }

    public String isSleeping() {
        if (getIsSleeping()) {
            return name + " еще не проснулся";
        }
        return name + " уже проснулся";
    }

    public void setChewingObject(Eatable object) {
        chewingObject = object;
    }   

    public String chew() {
        if (chewingObject instanceof Food) {
            throw new WrongFoodException(chewingObject.toString() + "should not be chewed !");
        }
        return name + " уже жует" + chewingObject.toString() + "с аппетитом";
    }
    
}
