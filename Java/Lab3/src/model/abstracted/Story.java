package model.abstracted;

import java.util.ArrayList;
import java.util.List;

public abstract class Story {
    protected List<Person> characters;

    public Story() {
        this.characters = new ArrayList<>();
    }

    public void addCharacter(Person person) {
        characters.add(person);
    }

    public abstract void tell();
}
