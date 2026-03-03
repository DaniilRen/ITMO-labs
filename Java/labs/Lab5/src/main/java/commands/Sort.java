package commands;

import java.util.ArrayList;
import java.util.Collections;

import managers.CollectionManager;
import models.Entity;
import util.transfer.Response;
import util.transfer.request.standart.StandartRequest;


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
