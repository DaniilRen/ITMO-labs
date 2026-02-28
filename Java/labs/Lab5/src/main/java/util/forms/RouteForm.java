package util.forms;

import models.Route;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import util.console.IOConsole;
import util.exceptions.InvalidFormException;
import models.Coordinates;
import models.Location2Dimension;
import models.Location3Dimension;


/**
 * Класс формы для пути.
 * @author Septyq
 */
public class RouteForm extends Form<Route> {
    private final IOConsole console;

    public RouteForm(IOConsole console) {
        this.console = console;
    }

    @Override
    public Route build() throws InvalidFormException {
        try {
            Route route = new Route(
                askName(),
                askCoordinates(),
                LocalDateTime.now(),
                askLocationFrom(),
                askLocationTo(),
                askDistance()
            );
            if (!route.validate()) throw new InvalidFormException("Route validation failed");
            return route;
        } catch (InvalidFormException e) {
            console.printError(e.getMessage());
            return null;
        }
    };

    private String askName() throws InvalidFormException {
        String name;
        while (true) {
            try {
                console.print("Enter Route name: ");
                name = console.getUserScanner().nextLine().trim();
                if (name.equals("")) throw new InvalidFormException("Name cannot be empty string");
                break;
            } catch (InvalidFormException e) {
                console.printError(e.getMessage());
            }
        }
        return name;
    };

    private Coordinates askCoordinates() throws InvalidFormException {
        return new CoordinatesForm(console).build();
    };

    private Location2Dimension askLocationFrom() throws InvalidFormException {
        return new Location2DimensionForm(console).build();
    };

    private Location3Dimension askLocationTo() throws InvalidFormException {
        return new Location3DimensionForm(console).build();
    };

    private int askDistance() throws InvalidFormException {
        int distance;

        while (true) {
            try {
                console.print("Enter Route distance: ");
                String strDistance = console.getUserScanner().nextLine().trim();
                distance = Integer.parseInt(strDistance);
                break;
            } catch (NoSuchElementException exception) {
                console.printError("Distance was not recognized!");
            } catch (NumberFormatException exception) {
                console.printError("Distance should be a number!");
            }
        }
        return distance;
    }
}
