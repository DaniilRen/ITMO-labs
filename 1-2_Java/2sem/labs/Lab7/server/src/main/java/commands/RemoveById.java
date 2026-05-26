package commands;

import java.util.List;

import auth.AuthManager;
import collection.CollectionManager;
import common.command.CommandAttribute;
import common.command.PublicityMarker;
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
public class RemoveById extends Command<IdRequest> {
    private static final long serialVersionUID = 8986213L;

    private final CollectionManager<Entity> collectionManager;
    private final AuthManager authManager;

    public RemoveById(CollectionManager<Entity> collectionManager, AuthManager authManager) {
        super(new CommandAttribute(
            "remove_by_id <ID>", 
            "удалить элемент из коллекции по ID",
            IdRequest.class,
            PublicityMarker.PRIVATE
            ));
        this.collectionManager = collectionManager;
        this.authManager = authManager;
    }

    public Response<?> execute(IdRequest request) {
        User userData = authManager.getCachedCredentials();
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
