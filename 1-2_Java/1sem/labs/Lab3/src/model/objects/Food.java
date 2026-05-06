package model.objects;
import model.abstracted.interfaces.Eatable;
import model.abstracted.interfaces.Lookable;

public record Food (String name) implements Eatable, Lookable {
    @Override
    public String toString() {
        return name;
    }
}
