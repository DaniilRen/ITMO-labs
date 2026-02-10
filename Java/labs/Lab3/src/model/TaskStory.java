package model;

import java.util.ArrayList;

import model.abstracted.Story;
import model.abstracted.enums.Distance;
import model.abstracted.enums.State;
import model.abstracted.enums.TimeUnit;
import model.abstracted.enums.Wish;
import model.abstracted.Path;
import model.abstracted.Speed;

import model.objects.Compartment;
import model.objects.Planet;
import model.objects.Rocket;

import model.characters.Neznaika;
import model.characters.Ponchik;

import model.exceptions.NotUniqueCompartmentsException;


public class TaskStory extends Story  {
    private final Neznaika neznaika;
    private final Ponchik ponchick;
    private final Rocket rocket;
    private final Speed rocketSpeed;
    private final Path rocketPath;

    public TaskStory() throws NotUniqueCompartmentsException {
        super();

        this.neznaika = new Neznaika();
        this.ponchick = new Ponchik();
        addCharacter(neznaika);
        addCharacter(ponchick);

        this.rocketSpeed = new Speed(Distance.TWELVE, TimeUnit.SECOND);
        this.rocketPath = new Path(Distance.FOUR_HUNDRED_THOUSAND, new Planet("Земля"),  new Planet("Луна"));
        ArrayList<Compartment> compartments = createCompartments();
        this.rocket = new Rocket("ракета", compartments);
    }

    private ArrayList<Compartment> createCompartments() {
        ArrayList<Compartment> compartments = new ArrayList<>();
        String[] names = {"прогулочный", "пищевой", "спальный"};
        for (String name : names) {
            compartments.add(new Compartment(name));
        }
        return compartments;
    }

    @Override
    public void tell() {
        ponchick.setLocation(new Compartment("спальный"));
        rocket.setSpeed(this.rocketSpeed);
        rocket.setPath(this.rocketPath);
        neznaika.setLocation(rocket);
        neznaika.setObservedObject(rocket.getPath().endPoint());        
        ponchick.setState(State.EATING);
        var rnd = Math.random();
        if (rnd < 0.3) {
            neznaika.setWish(Wish.EAT);
        } else   if (rnd >= 0.3 && rnd < 0.6)  {
            neznaika.setWish(Wish.SLEEP);
        } else {
            neznaika.setWish(Wish.REST);
        }
        // ponchick.setChewingObject(new Food("sdadsadas"));
        if (ponchick.getLocation() instanceof Compartment compartment) {
            if (compartment.equals(neznaika.getLocation())) {
                neznaika.setObservedObject(ponchick);
            }
        }    else {
                System.out.println(neznaika.getName() + " никого не увидел");
            }
    }
}