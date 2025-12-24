package model.objects;

import model.abstracted.interfaces.Location;
import model.abstracted.interfaces.Lookable;

public record Compartment (String type) implements Location, Lookable{
    @Override
    public String toString() {
        return type + " отсек";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null && !(obj instanceof Compartment)) {return false;}
        Compartment cobj = (Compartment) obj;
        return (cobj.type().equals(this.type())); 
    }
}
