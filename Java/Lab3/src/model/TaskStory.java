package model;

import java.util.ArrayList;

import model.abstracted.Distance;
import model.abstracted.Feeling;
import model.abstracted.Hour;
import model.abstracted.Story;
import model.abstracted.TimeUnit;
import model.abstracted.Wish;
import model.abstracted.Path;
import model.abstracted.Speed;
import model.abstracted.State;
import model.objects.Compartment;
import model.objects.Planet;
import model.objects.Rocket;

import model.characters.Neznaika;
import model.characters.Ponchik;
import model.exceptions.NotUniqueCompartments;


public class TaskStory extends Story  {
    private final Neznaika neznaika;
    private final Ponchik ponchick;
    private final Rocket rocket;
    private final Speed rocketSpeed;
    private final Path rocketPath;

    public TaskStory() throws NotUniqueCompartments {
        super();

        this.neznaika = new Neznaika();
        this.ponchick = new Ponchik();
        addCharacter(neznaika);
        addCharacter(ponchick);

        this.rocketSpeed = new Speed(Distance.TWELVE, TimeUnit.SECOND);
        this.rocketPath = new Path(Distance.FOUR_HUNDRED_THOUSAND, new Planet("Луна"), new Planet("Земля"));
        ArrayList<Compartment> compartments = createCompartments();
        this.rocket = new Rocket("ракета", compartments);
    }

    private ArrayList<Compartment> createCompartments() {
        ArrayList<Compartment> compartments = new ArrayList<>();
        String[] names = {"приборный", "пищевой", "носовой"};
        for (String name : names) {
            compartments.add(new Compartment(name));
        }
        return compartments;
    }

    @Override
    public void tell() {
        rocket.setSpeed(this.rocketSpeed);
        rocket.setPath(this.rocketPath);
        rocket.setIsRushing(true);
        rocket.describeRush();
        neznaika.setThoughts(rocket.describeApproaching());
        neznaika.describeThoughts();
        rocket.getPath().describe();
        System.out.println("прошло " + Hour.TWO.toString() + " или " + Hour.THREE.toString());
        neznaika.setObservedObject(rocket.getPath().endPoint());
        neznaika.describeObserve();
        neznaika.setFealing(Feeling.HUNGER);
        neznaika.describeFeeling();
        neznaika.setWish(Wish.EAT);
        neznaika.setRealisation(neznaika.isTimeToWish());
        neznaika.describeRealisation();
        Compartment foodCompartment = rocket.getCompartments().stream()
            .filter(c -> c.type().equals("пищевой"))
            .findFirst().orElse(null);
        neznaika.setLocation(foodCompartment);
        neznaika.describeLocation(" спустиля в ");
        ponchick.setState(State.EATING);
        // ponchick.setChewingObject(new Food("food"));
        neznaika.setNoticed(ponchick.describeSleeping() + " и " + ponchick.describeEating());
        neznaika.describeNotice();
    }
}