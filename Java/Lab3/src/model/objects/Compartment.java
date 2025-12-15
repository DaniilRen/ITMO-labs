package model.objects;

import model.abstracted.interfaces.Location;
import model.abstracted.interfaces.Lookable;

public record Compartment (String type) implements Location, Lookable{
    @Override
    public String toString() {
        return type + " отсек";
    }
}
