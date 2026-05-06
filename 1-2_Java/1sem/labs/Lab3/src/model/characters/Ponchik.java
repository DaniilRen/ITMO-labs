package model.characters;

import model.abstracted.Character;
import model.abstracted.enums.State;
import model.abstracted.interfaces.Eatable;
import model.exceptions.StateFieldNotSetException;
import model.objects.Food;


public class Ponchik extends Character {
    private Eatable chewingObject;

    public Ponchik() {
        super("Пончик");
    }

    @Override
    protected void describeLocation() {
        System.out.println(name + " гулял в " + this.location);
    }

    public String describeState() {
        this.checkState();
        String eating = this.getState().equals(State.EATING) ? 
            State.EATING + " " + this.getChewingObject() 
            : "ничего не ест";
        return this.state.equals(State.SLEEPING) ? 
            this.getName() + " еще не проснулся" : 
            this.getName() + " уже проснулся" + " и " + eating;
    }

    private void checkState() {
        if (this.getState() == null) {
            throw new StateFieldNotSetException("you should specify state before describing it !");
        }
    }

    public void setChewingObject(Eatable object) {
        this.chewingObject = object;
    }

    public Eatable getChewingObject() {
        return this.chewingObject == null ? new Food("что-то") : this.chewingObject;
    }
}
