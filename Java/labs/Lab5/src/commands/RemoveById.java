package commands;

import java.util.List;

import managers.CollectionManager;
import models.Route;
import util.Response;
import util.Status;

public class RemoveById extends Command {
    private final CollectionManager collectionManager;

    public RemoveById(CollectionManager collectionManager) {
        super("remove_by_id <ID>", "удалить элемент из коллекции по ID");
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(List<?> args) {
        try {
            if (args.size() == 1) {
                int id = Integer.parseInt((String) args.get(0));
                Route routeToRemove = (Route) collectionManager.getById(id);
                if (routeToRemove == null) {
                    return new Response<>(List.of("Item not found"), Status.ERROR);
                }
                collectionManager.removeFromCollection(routeToRemove);
                return new Response<>();
            } else {
                return new Response<>(List.of("Invalid argument length"), Status.ERROR);
            }
        } catch (NumberFormatException e) {
            return new Response<>(List.of("Invalid id"), Status.ERROR);
        }
    }
}
