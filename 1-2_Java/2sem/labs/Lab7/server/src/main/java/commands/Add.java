package commands;

import java.sql.SQLException;
import java.util.List;

import collection.CollectionManager;
import common.models.Entity;
import common.models.Route;
import common.transfer.Status;
import common.transfer.request.standart.EntityRequest;
import common.transfer.response.Response;
import database.service.DatabaseService;

/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 * @author Septyq
 */
public class Add extends Command<EntityRequest> {
    private static final long serialVersionUID = 1932432L;

    private final DatabaseService databaseService;
    private final CollectionManager<Entity> collectionManager;

    public Add(DatabaseService databaseService,  CollectionManager<Entity> collectionManager) {
        super(new CommandAttribute(
            "add {element}", 
            "добавить новый элемент в коллекцию", 
            EntityRequest.class
            ));
        this.collectionManager = collectionManager;
        this.databaseService = databaseService;
    }
    
    public Response<?> execute(EntityRequest request) {
        Entity entity = request.getEntity();
        try {
            databaseService.saveRoute((Route) entity);
            collectionManager.addToCollection(entity);
            return new Response<>(List.of("item added"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new Response<>(List.of("Error while adding item"), Status.ERROR);
        }
    }
}
