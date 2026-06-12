package commands;

import java.util.Iterator;
import java.util.List;

import auth.AuthManager;
import collection.CollectionManager;
import commands.util.AccessHandler;
import common.transfer.request.standart.EntityRequest;
import common.transfer.response.Response;
import common.command.CommandAttribute;
import common.command.PublicityMarker;
import common.models.Entity;
import common.models.Route;
import common.models.User;;


/**
 * Команда 'remove_lower {element}'. Удаляет из коллекции все элементы, меньшие, чем заданный.
 * @author Septyq
 */
public class RemoveLower extends Command<EntityRequest> {
    private static final long serialVersionUID = 982013478L;

    private final CollectionManager<Entity> collectionManager;
    private final AuthManager authManager;

    public RemoveLower(CollectionManager<Entity> collectionManager, AuthManager authManager) {
        super(new CommandAttribute(
            "remove_lower {element}", 
            "удалить из коллекции все элементы, меньшие, чем заданный",
            EntityRequest.class,
            PublicityMarker.PRIVATE
            ));
        this.collectionManager = collectionManager;
        this.authManager = authManager;
    }

    @Override
    public Response<?> execute(EntityRequest request) {
        User userData = authManager.getCachedCredentials();
        Route targetRoute = (Route) request.getEntity(); 
        
        Iterator<Entity> iterator = collectionManager.getCollection().iterator();
        int removedCount = 0;
        
        while (iterator.hasNext()) {
            Route route = (Route) iterator.next();
            if (AccessHandler.accessVerified(route, userData) && route.compareTo(targetRoute) < 0) {
                iterator.remove();
                removedCount++;
            }
        }
        
        return new Response<>(List.of("Removed " + removedCount + " elements"));
    }
}
