package util;

import models.Route;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Scanner;

import util.console.Console;
import util.exceptions.build.BuildException;
import util.exceptions.build.InvalidBuildFormException;
import util.exceptions.build.InvalidCoordinatesBuildException;
import util.exceptions.build.InvalidLocationBuildException;
import util.exceptions.build.InvalidNameBuildException;
import models.Coordinates;
import models.Location2Dimension;
import models.Location3Dimension;

public class Builder {
    private final Console console;
    private final Scanner scanner;

    public Builder(Console console, Scanner scanner) {
        this.console = console;
        this.scanner = scanner;
    }

    public Route build() throws BuildException {
        try {
            Route route = new Route(
                buildName(),
                buildCoordinates(),
                LocalDateTime.now(),
                buildLocation2Dimension(),
                buildLocation3Dimension()
            );
            if (route.validate() == false) throw new InvalidBuildFormException("Invalid build form");
            return route;
        } catch (BuildException e) {
            console.printError(e.getMessage());
            return null;
        }
    };

    private String buildName() throws InvalidNameBuildException {
        console.print("Enter name: ");
        String name = scanner.nextLine().trim();
        if (name.length() == 0) {
            throw new InvalidNameBuildException("Invalid name");
        }
        return name;
    };

    private Coordinates buildCoordinates() throws InvalidCoordinatesBuildException {
        try {
            console.print("Enter Coordinates X: ");
            Float x = Float.parseFloat(scanner.nextLine().trim());

            console.print("Enter Coordinates Y: ");
            Long y = Long.parseLong(scanner.nextLine().trim());

            return new Coordinates(x, y);
        } catch (NumberFormatException | NoSuchElementException e) {
            throw new InvalidCoordinatesBuildException("Coordinates should be numbers");
        }
    };

    private Location2Dimension buildLocation2Dimension() throws InvalidLocationBuildException {
        try {
            console.print("Enter From Location X: ");
            Integer x = Integer.parseInt(scanner.nextLine().trim());

            console.print("Enter From Location Y: ");
            Double y = Double.parseDouble(scanner.nextLine().trim());

            console.print("Enter From Location name: ");
            String name = scanner.nextLine().trim();
            if (name.isBlank()) { throw new InvalidLocationBuildException("Invalid Location name"); }

            return new Location2Dimension(x, y, name);
        } catch (NumberFormatException | NoSuchElementException e) {
            throw new InvalidLocationBuildException(e.getMessage());
        }
    };

    private Location3Dimension buildLocation3Dimension() throws InvalidLocationBuildException {
         try {
            console.print("Enter TO Location X: ");
            double x = Double.parseDouble(scanner.nextLine().trim());

            console.print("Enter TO Location Y: ");
            Double y = Double.parseDouble(scanner.nextLine().trim());

            console.print("Enter TO Location Z: ");
            Integer z = Integer.parseInt(scanner.nextLine().trim());

            console.print("Enter TO Location name: ");
            String name = scanner.nextLine().trim();
            if (name.isBlank()) { throw new InvalidLocationBuildException("Invalid Location name"); }

            return new Location3Dimension(x, y, z, name);
        } catch (NumberFormatException | NoSuchElementException e) {
            throw new InvalidLocationBuildException(e.getMessage());
        }
    };
}
