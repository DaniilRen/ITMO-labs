package commands;

import java.util.List;

import collection.CollectionManager;
import common.models.Entity;
import common.models.Route;
import common.models.User;
import common.transfer.Status;
import common.transfer.request.standart.IdRequest;
import common.transfer.response.Response;


/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции по ID.
 * @author Septyq
 */
public class RemoveById extends AuthAwareCommand<IdRequest> {
    private static final long serialVersionUID = 8986213L;

    private final CollectionManager<Entity> collectionManager;

    public RemoveById(CollectionManager<Entity> collectionManager) {
        super(new CommandAttribute(
            "remove_by_id <ID>", 
            "удалить элемент из коллекции по ID",
            IdRequest.class
            ));
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(IdRequest request, User userData) {
        Route route = (Route) collectionManager.getById(request.getId());
        if (route == null) {
            return new Response<>(List.of("Item not found"), Status.ERROR);
        }
        if (!(route.getAuthor().equals(userData.getName()))) {
            return new Response<>(List.of("You have no permission to remove this item"), Status.ERROR);
        }
        collectionManager.removeFromCollection(route);
        return new Response<>(List.of("element removed"));
    }
}
