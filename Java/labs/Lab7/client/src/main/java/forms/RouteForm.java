package forms;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import console.IOConsole;
import common.exceptions.InvalidFormException;
import common.exceptions.InvalidScriptException;
import common.models.Route;
import common.models.Coordinates;
import common.models.Location2Dimension;
import common.models.Location3Dimension;


/**
 * Класс формы для пути.
 * @author Septyq
 */
public class RouteForm extends Form<Route> {
    private final IOConsole console;
    private final boolean fileMode;
    private final String author;

    public RouteForm(IOConsole console, String author) {
        this.console = console;
        this.fileMode = console.fileMode();
        this.author = author;
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
                askDistance(),
                author
            );
            if (!route.validate()) throw new InvalidFormException("invalid route");
            return route;
        } catch (InvalidFormException | InvalidScriptException e) {
            console.printError(e.getMessage());
            return null;
        }
    };

    private String askName() throws InvalidScriptException {
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
                    throw new InvalidScriptException("Invalid input data in script -> operation stopped");
                } else {
                    console.printError("Name not recognized, enter it again");
                }
            }
        } while (!asked);

        return name;
    };

    private Coordinates askCoordinates() throws InvalidFormException, InvalidScriptException {
        return new CoordinatesForm(console).build();
    };

    private Location2Dimension askLocationFrom() throws InvalidFormException, InvalidScriptException {
        return new Location2DimensionForm(console).build();
    };

    private Location3Dimension askLocationTo() throws InvalidFormException, InvalidScriptException {
        return new Location3DimensionForm(console).build();
    };

    private int askDistance() throws InvalidScriptException {
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
                    throw new InvalidScriptException("Invalid input data in script -> operation stopped");
                } else {
                    console.printError("Distance was not recognized, enter it again");
                }
            }
        } while (!asked);

        return distance;
    }
}
