package model.objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import model.abstracted.Speed;
import model.abstracted.interfaces.Location;
import model.abstracted.interfaces.Lookable;
import model.exceptions.NotUniqueCompartmentsException;
import model.abstracted.Path;

public class Rocket implements Lookable, Location {
    private final ArrayList<Compartment> compartments;
    private final String name;
    private Speed speed;
    private Path path;

    public Rocket(String name, ArrayList<Compartment> compartments) throws NotUniqueCompartmentsException {
        this.name = name;
        this.checkUniqueCompartments(compartments);
        this.compartments = compartments;
    }

    public void setPath(Path path) {
        System.out.println(path);
        this.path = path;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public Path getPath() {
        return this.path;
    }

    public Speed getSpeed() {
        return speed;
    }

    public ArrayList<Compartment> getCompartments() {
        return this.compartments;
    }

    public void checkUniqueCompartments(ArrayList<Compartment> compartments) 
            throws NotUniqueCompartmentsException {
        Set<String> types = new HashSet<>();
        for (Compartment c : compartments) {
            if (!types.add(c.type())) {
                throw new NotUniqueCompartmentsException("compartments shall have unique 'type' field; got duplicate field: " + c.type());
            }
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}