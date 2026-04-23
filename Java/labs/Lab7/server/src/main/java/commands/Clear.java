package commands;

import common.models.Entity;

import java.util.List;

import common.transfer.request.standart.StandartRequest;
import common.transfer.response.Response;
import managers.collection.AbstractCollectionManager;

/**
 * Команда 'clear'. Очищает коллекцию.
 * @author Septyq
 */
public class Clear extends Command<StandartRequest> {
    private static final long serialVersionUID = 8932432L;

    private final AbstractCollectionManager<Entity> collectionManager;

    public Clear(AbstractCollectionManager<Entity> collectionManager) {
        super(new CommandAttribute(
            "clear", 
            "очистить коллекцию", 
            StandartRequest.class
            ));
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(StandartRequest request) {
        collectionManager.clearCollection();
        return new Response<>(List.of("collection cleared"));
    }
}
