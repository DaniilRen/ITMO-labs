package model;

import java.util.ArrayList;

import javax.print.DocFlavor.STRING;

import model.abstracted.Distance;
import model.abstracted.Hour;
import model.abstracted.Story;
import model.abstracted.TimeUnit;
import model.abstracted.Path;
import model.abstracted.Speed;
import model.objects.Planet;
import model.objects.Rocket;
import model.objects.Compartment;
import model.objects.Environment;

import model.characters.Neznaika;
import model.characters.Ponchik;

import model.exceptions.CharacterNotFoundException;
import model.exceptions.StoryException;


public class TaskStory extends Story {
    private final Neznaika neznaika;
    private final Ponchik ponchick;
    private final Rocket rocket;
    private final Speed rocketSpeed;
    private final Path rocketPath;
    private final Distance pathDistance;
    private final Planet moon;
    private final Planet earth;

    public TaskStory(Neznaika neznaika, Ponchik ponchick, Rocket rocket) {
        super();

        this.neznaika = neznaika;
        this.ponchick = ponchick;
        this.rocket = rocket;
        this.pathDistance = Distance.FOUR_HUNDRED_THOUSAND;
        this.rocketSpeed = new Speed(Distance.TWELVE, TimeUnit.SECOND);
        this.moon = new Planet("Луна");
        this.earth = new Planet("Земля");
        this.rocketPath = new Path(this.pathDistance, this.earth, this.moon);

        addCharacter(neznaika);
        addCharacter(ponchick);
    }

    @Override
    public void tell() {
        rocket.setSpeed(this.rocketSpeed);
        rocket.setPath(this.rocketPath);
        rocket.setIsRushing(true);
        rocket.DescribeRush();
        // neznaika.think(rocket.freeze() + " и " + rocket.approaching(moon, Measure.NOT_A_FINGER_HALF));
        
        // System.out.println("Это " + Environment.explains_with(
        //     "расстояние от " + moon.name() + " до " + earth.name() + " " +
        //     Measure.VERY_BIG.toString() + " -- " + Measure.APPROXIMATELY.toString() + " " + Distance.PLANET_DISTANCE.toString())
        //     );

        // System.out.println("При таком " + Measure.HUGE.toString() + " расстоянии скорость " +
        //     rocket.getSpeed() + " " + Measure.NOT_SO_BIG.toString() + " чтоб ее " + Environment.could_be("было заметить на глаз") +
        //     ", да еще и " + Environment.located_in(rocket)
        //     );
        
        // System.out.println(Environment.time_went(Hour.TWO) + " или " + Environment.time_went(Hour.THREE));
        // neznaika.lookAt(moon);
        // neznaika.couldStopLooking();
        // moon.attract(neznaika);

        // neznaika.feal("какое-то мучительное посасывание в животе");
        // neznaika.realise("наступила пора обедать");
        // neznaika.move_down_to(food_compartment.name());
        // neznaika.see(ponchick.isSleeping());
        // neznaika.see(ponchick.chew());
    }
}