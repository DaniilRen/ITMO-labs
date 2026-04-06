package commands;

import java.util.ArrayList;
import java.util.Collections;

import managers.CollectionManager;
import common.models.Entity;
import common.transfer.request.standart.StandartRequest;
import common.transfer.response.Response;


/**
 * Команда 'sort'. Сортирует коллекцию в естественном порядке.
 * @author Septyq
 */
public class Sort extends Command<StandartRequest> {
    private final CollectionManager<Entity> collectionManager;

    public Sort(CollectionManager<Entity> collectionManager) {
        super(new CommandAttribute(
            "sort", 
            "отсортировать коллекцию в естественном порядке",
            StandartRequest.class
            ));
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(StandartRequest request) {
        ArrayList<Entity> collection = (ArrayList<Entity>) collectionManager.getCollection();
        Collections.sort(collection);
        return new Response<>(collection);
    }
}
