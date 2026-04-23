package commands;

import java.util.List;

import common.models.Entity;
import common.transfer.request.standart.EntityRequest;
import common.transfer.response.Response;
import managers.collection.AbstractCollectionManager;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 * @author Septyq
 */
public class Add extends Command<EntityRequest> {
    private static final long serialVersionUID = 1932432L;

    private final AbstractCollectionManager<Entity> collectionManager;

    public Add(AbstractCollectionManager<Entity> collectionManager) {
        super(new CommandAttribute(
            "add {element}", 
            "добавить новый элемент в коллекцию", 
            EntityRequest.class
            ));
        this.collectionManager = collectionManager;
    }
    
    public Response<?> execute(EntityRequest request) {
        collectionManager.addToCollection(request.getEntity());
        return new Response<>(List.of("element added"));
    }
}
