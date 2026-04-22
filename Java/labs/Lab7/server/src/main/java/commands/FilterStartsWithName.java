package commands;

import java.util.ArrayList;
import java.util.Collection;

import managers.AbstractCollectionManager;
import common.models.Entity;
import common.models.Route;
import common.transfer.request.standart.StringRequest;
import common.transfer.response.Response;

/**
 * Команда 'filter_starts_with_name'. Выводит элементы, значение поля name которых начинается с заданной подстроки.
 * @author Septyq
 */
public class FilterStartsWithName extends Command<StringRequest> {
    private static final long serialVersionUID = 981734983L;

    private final AbstractCollectionManager<Entity> collectionManager;

    public FilterStartsWithName(AbstractCollectionManager<Entity> collectionManager) {
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
