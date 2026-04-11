package forms;

import java.util.NoSuchElementException;

import common.models.Location2Dimension;
import console.IOConsole;
import common.exceptions.InvalidFormException;
import common.exceptions.InvalidScriptException;


/**
 * Класс формы для локации (откуда).
 * @author Septyq
 */
public class Location2DimensionForm extends Form<Location2Dimension>{
    private final IOConsole console;
    private final boolean fileMode;

    public Location2DimensionForm(IOConsole console) {
        this.console = console;
        this.fileMode = console.fileMode();
    }

    public Location2Dimension build() throws InvalidFormException {
        try {
            Location2Dimension location = new Location2Dimension(askX(), askY(), askName());
            if (!location.validate()) throw new InvalidFormException("Location FROM validation failed");
            return location;   
        } catch (InvalidFormException | InvalidScriptException e) {
            throw new InvalidFormException(e.getMessage());
        }
    }

    private Integer askX() throws InvalidScriptException {
        Integer x = 0;
        boolean asked = false;
        do {
            try {
                console.print("Enter location FROM X (Integer): ");
                String strX = console.getUserScanner().nextLine().trim();
                if (fileMode) console.println(strX);
                x = Integer.parseInt(strX);
                asked = true;
                break;
            } catch (NoSuchElementException | NumberFormatException e) {
                if (fileMode) {
                    asked = true;
                    throw new InvalidScriptException("Invalid input data in script -> operation stopped");
                } else {
                    console.printError("Coordinate X was not recognized, enter it again");
                }
            }
        } while (!asked);
        
        return x;
    }

    private Double askY() throws InvalidScriptException {
        Double y = 0.0;
        boolean asked = false;
        do {
            try {   
                console.print("Enter location FROM Y (Double): ");
                String strY = console.getUserScanner().nextLine().trim();
                if (fileMode) console.println(strY);
                if (strY == "") {return null;}
                y = Double.parseDouble(strY);
                asked = true;
                break;
            } catch (NoSuchElementException | NumberFormatException e) {
                if (fileMode) {
                    asked = true;
                    throw new InvalidScriptException("Invalid input data in script -> operation stopped");
                } else {
                    console.printError("Coordinate Y was not recognized, enter it again");
                }
            }
        } while (!asked);

        return y;
    }

    private String askName() throws InvalidScriptException {
        String name = "";
        boolean asked = false;
        do {
            try {
                console.print("Enter location FROM name (String): ");
                name = console.getUserScanner().nextLine().trim();
                if (fileMode) console.println(name);
                if (name.equals("")) throw new InvalidFormException("");
                break;
            } catch (InvalidFormException e) {
                if (fileMode) {
                    asked = true;
                    throw new InvalidScriptException("Invalid input data in script -> operation stopped");
                } else {
                    console.printError("Name was not recognized, enter it again");
                }
            }          
        } while (!asked);
 
        return name;
    };
}
