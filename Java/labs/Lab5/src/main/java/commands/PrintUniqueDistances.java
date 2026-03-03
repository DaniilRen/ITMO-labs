package commands;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import managers.CollectionManager;
import models.Entity;
import models.Route;
import util.transfer.Response;
import util.transfer.request.standart.StandartRequest;;


/**
 * Команда 'print_unique_distance'. Выводит уникальные значения поля distance всех элементов в коллекции.
 * @author Septyq
 */
public class PrintUniqueDistances extends Command<StandartRequest> {
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
