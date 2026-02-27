package util.forms;

import java.util.NoSuchElementException;

import models.Coordinates;
import util.console.IOConsole;
import util.exceptions.InvalidFormException;

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

    public Float askX() {
        Float x;
        while (true) {
            try {
                console.println("Enter coordinate X (Float): ");
                String strX = console.getUserScanner().nextLine().trim();
                x = Float.parseFloat(strX);
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Coordinate X was not recognized!");
            } catch (NumberFormatException exception) {
                console.printError("Coordinate X should be a number!");
            }
        }
        return x;
    }

    public Long askY() {
        Long y;
        while (true) {
            try {
                console.println("Enter coordinate Y (Long): ");
                String strY = console.getUserScanner().nextLine().trim();
                if (strY == "") {return null;}
                y = Long.parseLong(strY);
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Coordinate Y was not recognized!");
            } catch (NumberFormatException exception) {
                console.printError("Coordinate Y should be a number!");
            }
        }
        return y;
    }
    
}
