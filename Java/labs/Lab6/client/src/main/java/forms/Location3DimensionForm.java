package util.forms;

import java.util.NoSuchElementException;

import models.Location3Dimension;
import util.console.IOConsole;
import util.exceptions.InvalidFormException;
import util.exceptions.ScriptSyntaxException;


/**
 * Класс формы для локации (куда).
 * @author Septyq
 */
public class Location3DimensionForm extends Form<Location3Dimension>{
    private final IOConsole console;
    private final boolean fileMode;

    public Location3DimensionForm(IOConsole console) {
        this.console = console;
        this.fileMode = console.fileMode();
    }

    public Location3Dimension build() throws InvalidFormException {
        try {
            Location3Dimension location = new Location3Dimension(askX(), askY(), askZ(), askName());
            if (!location.validate()) throw new InvalidFormException("Location TO validation failed");
            return location;   
        } catch (InvalidFormException | ScriptSyntaxException e) {
            throw new InvalidFormException(e.getMessage());
        }
    }

    private Double askX() throws ScriptSyntaxException {
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
                    throw new ScriptSyntaxException("Invalid input data in script -> operation stopped");
                } else {
                    console.printError("Coordinate X was not recognized, enter it again");
                }
            }
        } while (!asked);

        return x;
    }

    private Double askY() throws ScriptSyntaxException {
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
                    throw new ScriptSyntaxException("Invalid input data in script -> operation stopped");
                } else {
                    console.printError("Coordinate Y was not recognized, enter it again");
                }
            }
        } while (!asked);

        return y;
    }

    private Integer askZ() throws ScriptSyntaxException {
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
                    throw new ScriptSyntaxException("Invalid input data in script -> operation stopped");
                } else {
                    console.printError("Coordinate Z was not recognized, enter it again");
                }
            }  
        } while (!asked);

        return z;
    }

    private String askName() throws ScriptSyntaxException {
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
                    throw new ScriptSyntaxException("Invalid input data in script -> operation stopped");
                } else {
                    console.printError("Name was not recognized, enter it again");
                }
            }            
        } while (!asked);

        return name;
    };
}
