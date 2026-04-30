package commands;

import java.util.List;

import common.blueprints.UserData;
import common.models.Entity;
import common.models.Route;
import common.transfer.Status;
import common.transfer.request.standart.CombinedRequest;
import common.transfer.response.Response;
import managers.collection.AbstractCollectionManager;


/**
 * Команда 'update'. Обновляет значение элемента коллекции по ID.
 * @author Septyq
 */
public class Update extends AuthAwareCommand<CombinedRequest> {
    private static final long serialVersionUID = 19788876L;

    private final AbstractCollectionManager<Entity> collectionManager;

    public Update(AbstractCollectionManager<Entity> collectionManager) {
        super(new CommandAttribute(
            "update <ID> {element}", 
            "обновить значение элемента коллекции по ID",
            CombinedRequest.class
            ));
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(CombinedRequest request, UserData userData) {
        try {
            Integer id = request.getId();
            Entity entity = request.getEntity();
            entity.setId(id);
            Route route = (Route) collectionManager.getById(id); 
            if (route == null) {
                return new Response<>(List.of("Item not found"), Status.ERROR);
            }
            if (!(route.getAuthor().equals(userData.user()))) {
                return new Response<>(List.of("You have no permission to modify this item"), Status.ERROR);
            }
            Status result = collectionManager.updateById(id, entity);
            
            if (result == Status.OK) {
                return new Response<>(List.of("element updated"));
            } else {
                return new Response<>(List.of("Item not found"), result);
            }
        } catch (NumberFormatException e) {
            return new Response<>(List.of("Invalid id"), Status.ERROR);
        }
    }
}
