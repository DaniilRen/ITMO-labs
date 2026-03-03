package commands;

import java.util.List;

import managers.CollectionManager;
import models.Entity;
import util.transfer.Response;
import util.transfer.request.standart.EntityRequest;


/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 * @author Septyq
 */
public class Add extends Command<EntityRequest> {
    private final CollectionManager<Entity> collectionManager;

    public Add(CollectionManager<Entity> collectionManager) {
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
