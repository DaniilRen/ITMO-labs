package commands;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import managers.CollectionManager;
import models.Route;
import util.Response;
import util.Status;


/**
 * Команда 'print_unique_distance'. Выводит уникальные значения поля distance всех элементов в коллекции.
 * @author Septyq
 */
public class PrintUniqueDistances extends Command {
    private CollectionManager collectionManager;

    public PrintUniqueDistances(CollectionManager collectionManager) {
        super("print_unique_distance", "вывести уникальные значения поля distance всех элементов в коллекции");
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(List<?> args) {
        if (args.size() != 0) {
            return new Response<>(List.of("Invalid argument length"), Status.ERROR);
        } else {
            Set<Integer> uniqueDistances = collectionManager.getCollection().stream()
                .map(e -> ((Route)e).getDistance())
                .collect(Collectors.toSet());
            return new Response<>(List.of(uniqueDistances));
        }
    }
}
