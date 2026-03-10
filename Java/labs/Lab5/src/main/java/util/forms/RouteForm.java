package util.forms;

import models.Route;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import util.console.IOConsole;
import util.exceptions.InvalidFormException;
import util.exceptions.ScriptSyntaxException;
import models.Coordinates;
import models.Location2Dimension;
import models.Location3Dimension;


/**
 * Класс формы для пути.
 * @author Septyq
 */
public class RouteForm extends Form<Route> {
    private final IOConsole console;
    private final boolean fileMode;

    public RouteForm(IOConsole console) {
        this.console = console;
        this.fileMode = console.fileMode();
    }

    @Override
    public Route build() {
        try {
            Route route = new Route(
                askName(),
                askCoordinates(),
                LocalDateTime.now(),
                askLocationFrom(),
                askLocationTo(),
                askDistance()
            );
            if (!route.validate()) throw new InvalidFormException("Final Route validation failed");
            return route;
        } catch (InvalidFormException | ScriptSyntaxException e) {
            console.printError(e.getMessage());
            return null;
        }
    };

    private String askName() throws ScriptSyntaxException {
        String name = "";
        boolean asked = false;
        do {
            try {
                console.print("Enter Route name: ");
                name = console.getUserScanner().nextLine().trim();
                if (fileMode) console.println(name);
                if (name.equals("")) throw new InvalidFormException("");
                break;
            } catch (InvalidFormException e) {
                if (fileMode) {
                    asked = true;
                    throw new ScriptSyntaxException("Invalid input data in script -> operation stopped");
                } else {
                    console.printError("Name not recognized, enter it again");
                }
            }
        } while (!asked);

        return name;
    };

    private Coordinates askCoordinates() throws InvalidFormException, ScriptSyntaxException {
        return new CoordinatesForm(console).build();
    };

    private Location2Dimension askLocationFrom() throws InvalidFormException, ScriptSyntaxException {
        return new Location2DimensionForm(console).build();
    };

    private Location3Dimension askLocationTo() throws InvalidFormException, ScriptSyntaxException {
        return new Location3DimensionForm(console).build();
    };

    private int askDistance() throws ScriptSyntaxException {
        int distance = 0;
        boolean asked = false;
        do {
            try {
                console.print("Enter Route distance: ");
                String strDistance = console.getUserScanner().nextLine().trim();
                distance = Integer.parseInt(strDistance);
                if (fileMode) console.println(distance);
                asked = true;
                console.println("");
                break;
            } catch (NoSuchElementException | NumberFormatException exception) {
                if (fileMode) {
                    asked = true;
                    throw new ScriptSyntaxException("Invalid input data in script -> operation stopped");
                } else {
                    console.printError("Distance was not recognized, enter it again");
                }
            }
        } while (!asked);

        return distance;
    }
}
