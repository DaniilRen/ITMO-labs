import model.characters.Neznaika;
import model.characters.Ponchik;
import model.TaskStory;
import model.abstracted.Distance;
import model.objects.Rocket;


public class Main {
    public static void main(String[] args) {
        Neznaika neznaika = new Neznaika();
        Ponchik ponchick = new Ponchik();
        Rocket rocket = new Rocket("ракета", Distance.ROCKET_DISTANCE_PER_SECOND);

        TaskStory story = new TaskStory(neznaika, ponchick, rocket);
        story.tell();
    }
}