package commands;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import managers.CollectionManager;
import models.Route;
import util.Response;
import util.Status;


/**
 * Команда 'print_field_descending_distance'. Выводит значения поля distance всех элементов в порядке убывания.
 * @author Septyq
 */
public class PrintFieldDescendingDistance extends Command {
        private CollectionManager collectionManager;

    public PrintFieldDescendingDistance(CollectionManager collectionManager) {
        super("print_field_descending_distance", "вывести значения поля distance всех элементов в порядке убывания");
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(List<?> args) {
        if (args.size() != 0) {
            return new Response<>(List.of("Invalid argument length"), Status.ERROR);
        } else {
            List<Integer> distances = collectionManager.getCollection().stream()
                .map(e -> ((Route)e).getDistance())
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
            return new Response<>(List.of(distances));
        }
    }
}
