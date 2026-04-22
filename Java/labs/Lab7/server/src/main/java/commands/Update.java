package commands;

import java.util.List;

import managers.CollectionManager;
import common.models.Entity;
import common.transfer.Status;
import common.transfer.request.standart.CombinedRequest;
import common.transfer.response.Response;


/**
 * Команда 'update'. Обновляет значение элемента коллекции по ID.
 * @author Septyq
 */
public class Update extends Command<CombinedRequest> {
    private static final long serialVersionUID = 19788876L;

    private final CollectionManager<Entity> collectionManager;

    public Update(CollectionManager<Entity> collectionManager) {
        super(new CommandAttribute(
            "update <ID> {element}", 
            "обновить значение элемента коллекции по ID",
            CombinedRequest.class
            ));
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(CombinedRequest request) {
        try {
            Integer id = request.getId();
            Entity entity = request.getEntity();
            entity.setId(id);
            
            if (collectionManager.getById(id) == null) {
                return new Response<>(List.of("Item not found"), Status.ERROR);
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
