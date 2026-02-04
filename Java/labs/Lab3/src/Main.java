import model.TaskStory;
import model.exceptions.NotUniqueCompartmentsException;


public class Main {
    public static void main(String[] args) {
        try {
            TaskStory story = new TaskStory();
            story.tell();
        } catch(NotUniqueCompartmentsException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}