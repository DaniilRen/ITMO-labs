package model.characters;

import model.abstracted.Character;
import model.abstracted.enums.State;
import model.abstracted.interfaces.Eatable;
import model.exceptions.StateFieldNotSetException;


public class Ponchik extends Character {
    private Eatable chewingObject;

    public Ponchik() {
        super("Пончик");
    }

    @Override
    protected void describeLocation() {
        System.out.println(name + " находился в " + this.location);
    }

    public String describeSleeping() {
        this.checkState();
        if (this.state.equals(State.SLEEPING)) {
            return name + " еще не проснулся";
        }
        return name + " уже проснулся";
    }

    public String describeEating() {
        this.checkState();
        if (this.getState().equals(State.EATING)) {
            if (this.chewingObject == null) {
                return this.getName() + " " + State.EATING + " что-то";
            }
            return this.getName() + " " + State.EATING + " " + this.getChewingObject();
        }
        return this.getName() + " ничего не ест";
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
        return this.chewingObject;
    }
}
