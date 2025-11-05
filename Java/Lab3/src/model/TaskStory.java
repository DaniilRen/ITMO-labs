package model;

import model.abstracted.Story;
import model.exceptions.CharacterNotFoundException;
import model.exceptions.StoryException;
import model.tools.Shoes;

import java.util.Random;

public class TaskStory extends Story {
    private final Neznaika neznaika;
    private final Knopochka knopochka;
    private final Shoes shoes;

    public TaskStory(Neznaika neznaika, Knopochka knopochka, Shoes shoes) {
        super();
        this.neznaika = neznaika;
        this.knopochka = knopochka;
        this.shoes = shoes;
        addCharacter(neznaika);
        addCharacter(knopochka);
    }

    @Override
    public void tell() {
        try {
            if (new Random().nextBoolean()) {
                throw new StoryException("Something went wrong in the story!");
            }
            neznaika.wave();
            neznaika.lookAt(shoes);
            neznaika.speak("о том, как мечтал о волшебной палочке");
            knopochka.giveAdvide();
            System.out.println("но у " + neznaika.getName() + " ничего не вышло, так как он делал добро не бескорыстно");
        } catch (StoryException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            throw new CharacterNotFoundException("Character not found in the story!");
        }
    }
}