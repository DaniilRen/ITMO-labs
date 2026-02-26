package commands;

import java.util.ArrayList;
import java.util.List;

import managers.CollectionManager;
import util.Response;
import util.Status;
import models.Entity;
import models.Route;;

public class RemoveLower extends Command {
    private final CollectionManager collectionManager;

    public RemoveLower(CollectionManager collectionManager) {
        super("remove_lower {element}", "удалить из коллекции все элементы, меньшие, чем заданный");
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(List<?> args) {
        if (args.isEmpty()) {
            return new Response<>(Status.INPUT);
        } else if (args.size() == 1) {
            Route target_route = (Route) args.get(0);
            ArrayList<Route> collection = collectionManager.getCollection();
            for (Entity entity: collection) {
                Route route = (Route) entity;
                if (route.compareTo(target_route) < 0) {
                    collectionManager.removeFromCollection(route);
                }
            }
            return new Response<String>();
        } else {
            return new Response<>(List.of("Invalid argument length"), Status.ERROR);
        }
    }
}
