package commands;

import managers.CollectionManager;
import common.models.Entity;

import java.util.List;

import common.transfer.request.standart.StandartRequest;
import common.transfer.response.Response;

/**
 * Команда 'clear'. Очищает коллекцию.
 * @author Septyq
 */
public class Clear extends Command<StandartRequest> {
    private static final long serialVersionUID = 8932432L;

    private final CollectionManager<Entity> collectionManager;

    public Clear(CollectionManager<Entity> collectionManager) {
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
