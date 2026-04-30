package commands;

import java.util.List;

import common.models.Entity;
import common.transfer.Status;
import common.transfer.request.standart.EntityRequest;
import common.transfer.response.Response;
import managers.collection.AbstractCollectionManager;
import managers.database.AbstractDatabaseManager;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 * @author Septyq
 */
public class Add extends Command<EntityRequest> {
    private static final long serialVersionUID = 1932432L;

    private final AbstractDatabaseManager databaseManager;
    private final AbstractCollectionManager<Entity> collectionManager;

    public Add(AbstractDatabaseManager databaseManager,  AbstractCollectionManager<Entity> collectionManager) {
        super(new CommandAttribute(
            "add {element}", 
            "добавить новый элемент в коллекцию", 
            EntityRequest.class
            ));
        this.collectionManager = collectionManager;
        this.databaseManager = databaseManager;
    }
    
    public Response<?> execute(EntityRequest request) {
        Entity entity = request.getEntity();
        int id = databaseManager.insertEntity(entity);
        if (id < 0) {
            return new Response<>(List.of("Error while adding item"), Status.ERROR);
        }
        entity.setId(id);
        collectionManager.addToCollection(entity);
        return new Response<>(List.of("item added"));
    }
}
