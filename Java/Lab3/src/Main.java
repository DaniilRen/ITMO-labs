import model.Knopochka;
import model.Neznaika;
import model.TaskStory;
import model.objects.ShoeType;
import model.objects.Shoes;

public class Main {
    public static void main(String[] args) {
        Neznaika neznaika = new Neznaika();
        Knopochka knopochka = new Knopochka();
        Shoes shoes = new Shoes(ShoeType.RED);

        TaskStory story = new TaskStory(neznaika, knopochka, shoes);
        story.tell();
    }
}