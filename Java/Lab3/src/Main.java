import model.characters.Neznaika;
import model.characters.Ponchik;

import java.util.ArrayList;

import model.TaskStory;
import model.objects.Compartment;
import model.objects.Rocket;


public class Main {
    public static void main(String[] args) {
        Neznaika neznaika = new Neznaika();
        Ponchik ponchick = new Ponchik();

        ArrayList<Compartment> compartments = new ArrayList<Compartment>();
        String[] compartmentNames = {"приборный", "пищевой", "носовой"};
        for (String compartmentName: compartmentNames) {
            compartments.add(new Compartment(compartmentName));
        }
        Rocket rocket = new Rocket("ракета", compartments);

        TaskStory story = new TaskStory(neznaika, ponchick, rocket);
        story.tell();
    }
}