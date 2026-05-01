package commands;

import java.sql.SQLException;
import java.util.List;

import collection.CollectionManager;
import common.models.Entity;
import common.models.Route;
import common.models.User;
import common.transfer.Status;
import common.transfer.request.standart.CombinedRequest;
import common.transfer.response.Response;
import database.service.DatabaseService;

/**
 * Команда 'update'. Обновляет значение элемента коллекции по ID.
 * @author Septyq
 */
public class Update extends AuthAwareCommand<CombinedRequest> {
    private static final long serialVersionUID = 19788876L;

    private final DatabaseService databaseService;
    private final CollectionManager<Entity> collectionManager;

    public Update(DatabaseService databaseService,  CollectionManager<Entity> collectionManager) {
        super(new CommandAttribute(
            "update <ID> {element}", 
            "обновить значение элемента коллекции по ID",
            CombinedRequest.class
            ));
        this.databaseService = databaseService;
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(CombinedRequest request, User userData) {
        Integer id = request.getId();
        Entity entity = request.getEntity();
        entity.setId(id);

        Route existingRoute = (Route) collectionManager.getById(id); 
        if (existingRoute == null) {
            return new Response<>(List.of("Item not found"), Status.ERROR);
        }
        if (!(existingRoute.getAuthor().equals(userData.getName()))) {
            return new Response<>(List.of("You have no permission to modify this item"), Status.ERROR);
        }

        try {
            Route newRoute = (Route) entity;
            databaseService.updateRoute(newRoute);

            Status result = collectionManager.updateById(id, entity);
            
            if (result == Status.OK) {
                return new Response<>(List.of("element updated"));
            } else {
                return new Response<>(List.of("Item not found"), result);
            }
        } catch (SQLException e) {
            return new Response<>(List.of("Error while adding item"), Status.ERROR);
        }
    }
}
