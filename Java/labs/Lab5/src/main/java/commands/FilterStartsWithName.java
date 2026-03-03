package commands;

import java.util.ArrayList;
import java.util.Collection;

import managers.CollectionManager;
import models.Entity;
import models.Route;
import util.transfer.Response;
import util.transfer.request.standart.StringRequest;

/**
 * Команда 'filter_starts_with_name'. Выводит элементы, значение поля name которых начинается с заданной подстроки.
 * @author Septyq
 */
public class FilterStartsWithName extends Command<StringRequest> {
    private final CollectionManager<Entity> collectionManager;

    public FilterStartsWithName(CollectionManager<Entity> collectionManager) {
        super(new CommandAttribute(
            "filter_starts_with_name name <name>", 
            "вывести элементы, значение поля name которых начинается с заданной подстроки",
            StringRequest.class
            ));
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(StringRequest request) {
        Collection<Entity> filteredCollection = new ArrayList<>();
        Collection<Entity> collection = collectionManager.getCollection();
        for (Entity entity: collection) {
            Route route = (Route) entity;
            if (route.getName().startsWith(request.getRow())) {
                filteredCollection.add(route);
            }
        }
        return new Response<>(new ArrayList<>(filteredCollection));
    }
}
