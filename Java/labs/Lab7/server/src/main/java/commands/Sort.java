package commands;

import java.util.ArrayList;
import java.util.Collections;

import common.models.Entity;
import common.transfer.request.standart.StandartRequest;
import common.transfer.response.Response;
import managers.collection.AbstractCollectionManager;


/**
 * Команда 'sort'. Сортирует коллекцию в естественном порядке.
 * @author Septyq
 */
public class Sort extends Command<StandartRequest> {
    private static final long serialVersionUID = 8765332L;

    private final AbstractCollectionManager<Entity> collectionManager;

    public Sort(AbstractCollectionManager<Entity> collectionManager) {
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
