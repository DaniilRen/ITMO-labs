package forms;

import java.util.NoSuchElementException;

import common.models.LocationTo;
import console.IOConsole;
import common.exceptions.InvalidFormException;
import common.exceptions.InvalidScriptException;


/**
 * Класс формы для локации (куда).
 * @author Septyq
 */
public class LocationToForm extends Form<LocationTo>{
    private final IOConsole console;
    private final boolean fileMode;

    public LocationToForm(IOConsole console) {
        this.console = console;
        this.fileMode = console.fileMode();
    }

    public LocationTo build() throws InvalidFormException {
        try {
            LocationTo location = new LocationTo(askX(), askY(), askZ(), askName());
            if (!location.validate()) throw new InvalidFormException("Location TO validation failed");
            return location;   
        } catch (InvalidFormException | InvalidScriptException e) {
            throw new InvalidFormException(e.getMessage());
        }
    }

    private Double askX() throws InvalidScriptException {
        Double x = 0.0;
        boolean asked = false;
        do {
             try {
                console.print("Enter location TO X (Double): ");
                String strX = console.getUserScanner().nextLine().trim();
                if (fileMode) console.println(strX);
                if (strX == "") {return null;}
                x = Double.parseDouble(strX);
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
                console.print("Enter location TO Y (Double): ");
                String strY = console.getUserScanner().nextLine().trim();
                if (fileMode) console.println(strY);
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

    private Integer askZ() throws InvalidScriptException {
        Integer z = 0;
        boolean asked = false;
        do {
            try {
                console.print("Enter location TO Z (Integer): ");
                String strZ = console.getUserScanner().nextLine().trim();
                if (fileMode) console.println(strZ);
                z = Integer.parseInt(strZ);
                asked = true;
                break;
            } catch (NoSuchElementException | NumberFormatException e) {
                if (fileMode) {
                    asked = true;
                    throw new InvalidScriptException("Invalid input data in script -> operation stopped");
                } else {
                    console.printError("Coordinate Z was not recognized, enter it again");
                }
            }  
        } while (!asked);

        return z;
    }

    private String askName() throws InvalidScriptException {
        String name = "";
        boolean asked = false;
        do {
            try {
                console.print("Enter location TO name (String): ");
                name = console.getUserScanner().nextLine().trim();
                if (fileMode) console.println(name);
                if (name.equals("")) throw new InvalidFormException("");
                asked = true;
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
