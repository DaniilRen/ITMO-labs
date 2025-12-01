package model.objects;
import model.abstracted.Eatable;
import model.abstracted.Lookable;

public record Food (String name) implements Eatable, Lookable {}
