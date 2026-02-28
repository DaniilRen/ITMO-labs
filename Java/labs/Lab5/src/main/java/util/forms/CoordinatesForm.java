package util.forms;

import java.util.NoSuchElementException;

import models.Coordinates;
import util.console.IOConsole;
import util.exceptions.InvalidFormException;

/**
 * Класс формы для координат.
 * @author Septyq
 */
public class CoordinatesForm extends Form<Coordinates> {
    private final IOConsole console;

    public CoordinatesForm(IOConsole console) {
        this.console = console;
    }

    public Coordinates build() throws InvalidFormException {
        Coordinates coordinates = new Coordinates(askX(), askY());
        if (!coordinates.validate()) throw new InvalidFormException("Coordinates validation failed");
        return coordinates;
    }

    private Float askX() {
        Float x = Float.valueOf(0);
        boolean asked = false;
        do {
            try {
                console.println("Enter coordinate X (Float): ");
                String strX = console.getUserScanner().nextLine().trim();
                x = Float.parseFloat(strX);
                asked = true;
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Coordinate X was not recognized!");
            } catch (NumberFormatException exception) {
                console.printError("Coordinate X should be a number!");
            }  
        } while (!asked);

        return x;
    }

    private Long askY() {
        Long y = Long.valueOf(0);
        boolean asked = false;
        do {
            try {
                console.println("Enter coordinate Y (Long): ");
                String strY = console.getUserScanner().nextLine().trim();
                if (strY == "") {return null;}
                y = Long.parseLong(strY);
                asked = true;
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Coordinate Y was not recognized!");
            } catch (NumberFormatException exception) {
                console.printError("Coordinate Y should be a number!");
            }
        } while (!asked);
    
        return y;
    }
    
}
