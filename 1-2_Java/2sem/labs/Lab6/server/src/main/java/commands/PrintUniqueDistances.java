package commands;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import managers.CollectionManager;
import common.models.Entity;
import common.models.Route;
import common.transfer.request.standart.StandartRequest;
import common.transfer.response.Response;;


/**
 * Команда 'print_unique_distance'. Выводит уникальные значения поля distance всех элементов в коллекции.
 * @author Septyq
 */
public class PrintUniqueDistances extends Command<StandartRequest> {
    private static final long serialVersionUID = 88282837L;

    private CollectionManager<Entity> collectionManager;

    public PrintUniqueDistances(CollectionManager<Entity> collectionManager) {
        super(new CommandAttribute(
            "print_unique_distance",
            "вывести уникальные значения поля distance всех элементов в коллекции",
            StandartRequest.class
            ));
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(StandartRequest request) {
        Set<Integer> uniqueDistances = collectionManager.getCollection().stream()
            .map(e -> ((Route)e).getDistance())
            .collect(Collectors.toSet());
        return new Response<>(List.of(uniqueDistances));
    }
}
