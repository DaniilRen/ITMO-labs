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
        Double x;
        while (true) {
            try {
                console.println("Enter location TO X (Double): ");
                String strX = console.getUserScanner().nextLine().trim();
                if (strX == "") {return null;}
                x = Double.parseDouble(strX);
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Coordinate X was not recognized!");
            } catch (NumberFormatException exception) {
                console.printError("Coordinate X should be a number!");
            }
        }
        return x;
    }

    private Double askY() {
        Double y;
        while (true) {
            try {
                console.println("Enter location TO Y (Double): ");
                String strY = console.getUserScanner().nextLine().trim();
                y = Double.parseDouble(strY);
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Coordinate Y was not recognized!");
            } catch (NumberFormatException exception) {
                console.printError("Coordinate Y should be a number!");
            }
        }
        return y;
    }

    private Integer askZ() {
        Integer z;
        while (true) {
            try {
                console.println("Enter location TO Z (Integer): ");
                String strZ = console.getUserScanner().nextLine().trim();
                z = Integer.parseInt(strZ);
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Coordinate Y was not recognized!");
            } catch (NumberFormatException exception) {
                console.printError("Coordinate Y should be a number!");
            }
        }
        return z;
    }

    private String askName() throws InvalidFormException {
        String name;
        while (true) {
            try {
                console.print("Enter location TO name (String): ");
                name = console.getUserScanner().nextLine().trim();
                if (name.equals("")) throw new InvalidFormException("Name cannot be empty string");
                break;
            } catch (InvalidFormException e) {
                console.printError(e.getMessage());
            }
        }
        return name;
    };
}
