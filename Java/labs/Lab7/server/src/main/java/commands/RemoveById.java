package commands;

import java.util.List;

import common.blueprints.UserData;
import common.models.Entity;
import common.models.Route;
import common.transfer.Status;
import common.transfer.request.standart.IdRequest;
import common.transfer.response.Response;
import managers.collection.AbstractCollectionManager;


/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции по ID.
 * @author Septyq
 */
public class RemoveById extends AuthAwareCommand<IdRequest> {
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

    public Response<?> execute(IdRequest request, UserData userData) {
        Route route = (Route) collectionManager.getById(request.getId());
        if (route == null) {
            return new Response<>(List.of("Item not found"), Status.ERROR);
        }
        if (!(route.getAuthor().equals(userData.user()))) {
            return new Response<>(List.of("You have no permission to remove this item"), Status.ERROR);
        }
        collectionManager.removeFromCollection(route);
        return new Response<>(List.of("element removed"));
    }
}
