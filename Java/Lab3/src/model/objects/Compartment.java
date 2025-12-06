package model.objects;

import model.abstracted.interfaces.Location;

public record Compartment (String type) implements Location{
    @Override
    public String toString() {
        return type + " отсек";
    }
}
