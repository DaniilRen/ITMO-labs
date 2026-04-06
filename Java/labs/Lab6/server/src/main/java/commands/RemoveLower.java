package commands;

import java.util.Iterator;
import java.util.List;

import managers.CollectionManager;
import common.transfer.request.standart.EntityRequest;
import common.transfer.response.Response;
import common.models.Entity;
import common.models.Route;;


/**
 * Команда 'remove_lower {element}'. Удаляет из коллекции все элементы, меньшие, чем заданный.
 * @author Septyq
 */
public class RemoveLower extends Command<EntityRequest> {
    private final CollectionManager<Entity> collectionManager;

    public RemoveLower(CollectionManager<Entity> collectionManager) {
        super(new CommandAttribute(
            "remove_lower {element}", 
            "удалить из коллекции все элементы, меньшие, чем заданный",
            EntityRequest.class
            ));
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(EntityRequest request) {
        Route targetRoute = (Route) request.getEntity(); 
        
        Iterator<Entity> iterator = collectionManager.getCollection().iterator();
        int removedCount = 0;
        
        while (iterator.hasNext()) {
            Route route = (Route) iterator.next();
            if (route.compareTo(targetRoute) < 0) {
                iterator.remove();
                removedCount++;
            }
        }
        
        return new Response<>(List.of("Removed " + removedCount + " elements"));
    }
}
