package util.forms;

import java.util.NoSuchElementException;

import models.Location2Dimension;
import util.console.IOConsole;
import util.exceptions.InvalidFormException;

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

    public Integer askX() {
        Integer x;
        while (true) {
            try {
                console.println("Enter coordinate X:");
                String strX = console.getUserScanner().nextLine().trim();
                x = Integer.parseInt(strX);
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Coordinate X was not recognized!");
            } catch (NumberFormatException exception) {
                console.printError("Coordinate X should be a number!");
            }
        }
        return x;
    }

    public double askY() {
        double y;
        while (true) {
            try {
                console.println("Enter coordinate Y:");
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

    private String askName() throws InvalidFormException {
        String name;
        while (true) {
            try {
                console.print("Enter name: ");
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
