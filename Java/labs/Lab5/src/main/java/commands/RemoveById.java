package commands;

import java.util.List;

import managers.CollectionManager;
import models.Entity;
import models.Route;
import util.Status;
import util.transfer.Response;
import util.transfer.request.standart.IdRequest;


/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции по ID.
 * @author Septyq
 */
public class RemoveById extends Command<IdRequest> {
    private final CollectionManager<Entity> collectionManager;

    public RemoveById(CollectionManager<Entity> collectionManager) {
        super(new CommandAttribute(
            "remove_by_id <ID>", 
            "удалить элемент из коллекции по ID",
            IdRequest.class
            ));
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(IdRequest request) {
        Route routeToRemove = (Route) collectionManager.getById(request.getId());
        if (routeToRemove == null) {
            return new Response<>(List.of("Item not found"), Status.ERROR);
        }
        collectionManager.removeFromCollection(routeToRemove);
        return new Response<>(List.of("element removed"));
    }
}
