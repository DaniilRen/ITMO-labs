package commands;

import java.util.ArrayList;
import java.util.List;

import managers.CollectionManager;
import models.Entity;
import models.Route;
import util.Response;
import util.Status;

public class FilterStartsWithName extends Command {
    private final CollectionManager collectionManager;

    public FilterStartsWithName(CollectionManager collectionManager) {
        super("filter_starts_with_name name <name>", "вывести элементы, значение поля name которых начинается с заданной подстроки");
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(List<?> args) {
        if (args.size() != 1) {
            return new Response<>(List.of("Invalid argument length"), Status.ERROR);
        } else {
            String name = (String) args.get(0);
            ArrayList<Route> filteredCollection = new ArrayList<>();
            ArrayList<Route> collection = collectionManager.getCollection();
            for (Entity entity: collection) {
                Route route = (Route) entity;
                if (route.getName().startsWith(name)) {
                    filteredCollection.add(route);
                }
            }
            return new Response<>(filteredCollection);
        }
    }
}
