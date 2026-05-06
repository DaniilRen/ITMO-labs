package model.abstracted;

import java.util.ArrayList;
import java.util.List;

public abstract class Story {
    protected List<Character> characters;

    public Story() {
        this.characters = new ArrayList<>();
    }

    public void addCharacter(Character character) {
        characters.add(character);
    }

    public abstract void tell();
}
