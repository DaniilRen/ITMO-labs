package util.forms;

import java.util.NoSuchElementException;

import models.Location3Dimension;
import util.console.IOConsole;
import util.exceptions.InvalidFormException;


/**
 * Класс формы для локации (куда).
 * @author Septyq
 */
public class Location3DimensionForm extends Form<Location3Dimension>{
    private final IOConsole console;

    public Location3DimensionForm(IOConsole console) {
        this.console = console;
    }

    public Location3Dimension build() throws InvalidFormException {
        Location3Dimension location = new Location3Dimension(askX(), askY(), askZ(), askName());
        if (!location.validate()) throw new InvalidFormException("Location To validation failed");
        return location;
    }

    private Double askX() {
        Double x = 0.0;
        boolean asked = false;
        do {
             try {
                console.println("Enter location TO X (Double): ");
                String strX = console.getUserScanner().nextLine().trim();
                if (strX == "") {return null;}
                x = Double.parseDouble(strX);
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
                console.println("Enter location TO Y (Double): ");
                String strY = console.getUserScanner().nextLine().trim();
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

    private Integer askZ() {
        Integer z = 0;
        boolean asked = false;
        do {
            try {
                console.println("Enter location TO Z (Integer): ");
                String strZ = console.getUserScanner().nextLine().trim();
                z = Integer.parseInt(strZ);
                asked = true;
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Coordinate Y was not recognized!");
            } catch (NumberFormatException exception) {
                console.printError("Coordinate Y should be a number!");
            }            
        } while (!asked);

        return z;
    }

    private String askName() throws InvalidFormException {
        String name = "";
        boolean asked = false;
        do {
            try {
                console.print("Enter location TO name (String): ");
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
