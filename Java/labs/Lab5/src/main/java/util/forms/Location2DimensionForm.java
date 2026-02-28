package util.forms;

import java.util.NoSuchElementException;

import models.Location2Dimension;
import util.console.IOConsole;
import util.exceptions.InvalidFormException;


/**
 * Класс формы для локации (откуда).
 * @author Septyq
 */
public class Location2DimensionForm extends Form<Location2Dimension>{
    private final IOConsole console;

    public Location2DimensionForm(IOConsole console) {
        this.console = console;
    }

    public Location2Dimension build() throws InvalidFormException {
        Location2Dimension location = new Location2Dimension(askX(), askY(), askName());
        if (!location.validate()) throw new InvalidFormException("Location From validation failed");
        return location;
    }

    private Integer askX() {
        Integer x = 0;
        boolean asked = false;
        do {
            try {
                console.println("Enter location FROM X (Integer): ");
                String strX = console.getUserScanner().nextLine().trim();
                x = Integer.parseInt(strX);
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

    private Double askY() {
        Double y = 0.0;
        boolean asked = false;
        do {
            try {   
                console.println("Enter location FROM Y (Double): ");
                String strY = console.getUserScanner().nextLine().trim();
                if (strY == "") {return null;}
                y = Double.parseDouble(strY);
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

    private String askName() throws InvalidFormException {
        String name = "";
        boolean asked = false;
        do {
            try {
                console.print("Enter location FROM name (String): ");
                name = console.getUserScanner().nextLine().trim();
                if (name.equals("")) throw new InvalidFormException("Name cannot be empty string");
                asked = true;
                break;
            } catch (InvalidFormException e) {
                console.printError(e.getMessage());
            }          
        } while (!asked);
 
        return name;
    };
}
