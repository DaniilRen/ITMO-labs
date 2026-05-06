package commands;

import java.util.ArrayList;

import collection.CollectionManager;
import common.models.Entity;
import common.transfer.request.standart.StandartRequest;
import common.transfer.response.Response;


/**
 * Команда 'show'. Выводит все элементы коллекции.
 * @author Septyq
 */
public class Show extends Command<StandartRequest> {
    private static final long serialVersionUID = 6099871L;

    private final CollectionManager<Entity>collectionManager;

    public Show(CollectionManager<Entity> collectionManager) {
        super(new CommandAttribute(
            "show", 
            "вывести все элементы коллекции",
            StandartRequest.class
            ));
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(StandartRequest request) {
        return new Response<>(new ArrayList<>(collectionManager.getCollection()));
    }
}
