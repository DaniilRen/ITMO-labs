package commands;

import java.util.Iterator;
import java.util.List;

import collection.CollectionManager;
import common.transfer.request.standart.EntityRequest;
import common.transfer.response.Response;
import common.models.Entity;
import common.models.Route;
import common.models.User;;


/**
 * Команда 'remove_lower {element}'. Удаляет из коллекции все элементы, меньшие, чем заданный.
 * @author Septyq
 */
public class RemoveLower extends AuthAwareCommand<EntityRequest> {
    private static final long serialVersionUID = 982013478L;

    private final CollectionManager<Entity> collectionManager;

    public RemoveLower(CollectionManager<Entity> collectionManager) {
        super(new CommandAttribute(
            "remove_lower {element}", 
            "удалить из коллекции все элементы, меньшие, чем заданный",
            EntityRequest.class
            ));
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(EntityRequest request, User userData) {
        Route targetRoute = (Route) request.getEntity(); 
        
        Iterator<Entity> iterator = collectionManager.getCollection().iterator();
        int removedCount = 0;
        
        while (iterator.hasNext()) {
            Route route = (Route) iterator.next();
            if (route.getAuthor().equals(userData.getName()) && route.compareTo(targetRoute) < 0) {
                iterator.remove();
                removedCount++;
            }
        }
        
        return new Response<>(List.of("Removed " + removedCount + " elements"));
    }
}
