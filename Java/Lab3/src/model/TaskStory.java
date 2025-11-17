package model;

import model.abstracted.Distance;
import model.abstracted.Hour;
import model.abstracted.Measure;
import model.abstracted.Story;

import model.objects.Planet;
import model.objects.Rocket;
import model.objects.Compartment;
import model.objects.Environment;

import model.characters.Neznaika;
import model.characters.Ponchik;

import model.exceptions.CharacterNotFoundException;
import model.exceptions.StoryException;

import java.util.Random;


public class TaskStory extends Story {
    private final Neznaika neznaika;
    private final Ponchik ponchick;
    private final Rocket rocket;

    private final Planet moon;
    private final Planet earth;
    private final Compartment food_compartment; 

    public TaskStory(Neznaika neznaika, Ponchik ponchick, Rocket rocket) {
        super();

        this.neznaika = neznaika;
        this.ponchick = ponchick;
        this.rocket = rocket;

        this.moon = new Planet("Луна");
        this.earth = new Planet("Земля");
        this.food_compartment = new Compartment("пищевой отсек");

        addCharacter(neznaika);
        addCharacter(ponchick);
    }

    @Override
    public void tell() {
        try {
            if (new Random().nextBoolean()) {
                throw new StoryException("Something went wrong in the story!");
            }

            rocket.rush(Measure.SCARY);
            neznaika.think(rocket.freeze() + " и " + rocket.approaching(moon, Measure.NOT_A_FINGER_HALF));
            
            System.out.println("Это " + Environment.explains_with(
                "расстояние от " + moon.name() + " до " + earth.name() + " " +
                Measure.VERY_BIG.toString() + " -- " + Measure.APPROXIMATELY.toString() + " " + Distance.PLANET_DISTANCE.toString())
                );

            System.out.println("При таком " + Measure.HUGE.toString() + " расстоянии скорость " +
                rocket.getSpeed() + " " + Measure.NOT_SO_BIG.toString() + " чтоб ее " + Environment.could_be("было заметить на глаз") +
                ", да еще и " + Environment.located_in(rocket)
                );
            
            System.out.println(Environment.time_went(Hour.TWO) + " или " + Environment.time_went(Hour.THREE));
            neznaika.lookAt(moon);
            neznaika.couldStopLooking();
            moon.attract(neznaika);

            neznaika.feal("какое-то мучительное посасывание в животе");
            neznaika.realise("наступила пора обедать");
            neznaika.move_down_to(food_compartment.name());
            neznaika.see(ponchick.isWokenUp());
            neznaika.see(ponchick.eat(Measure.WITH_APPETITE));

        } catch (StoryException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            throw new CharacterNotFoundException("Character not found in the story!");
        }
    }
}