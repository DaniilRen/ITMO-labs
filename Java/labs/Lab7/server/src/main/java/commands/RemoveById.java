package commands;

import java.util.List;

import managers.AbstractCollectionManager;
import common.models.Entity;
import common.models.Route;
import common.transfer.Status;
import common.transfer.request.standart.IdRequest;
import common.transfer.response.Response;


/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции по ID.
 * @author Septyq
 */
public class RemoveById extends Command<IdRequest> {
    private static final long serialVersionUID = 8986213L;

    private final AbstractCollectionManager<Entity> collectionManager;

    public RemoveById(AbstractCollectionManager<Entity> collectionManager) {
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
