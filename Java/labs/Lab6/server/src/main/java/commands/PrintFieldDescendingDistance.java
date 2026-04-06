package commands;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import managers.CollectionManager;
import common.models.Entity;
import common.models.Route;
import common.transfer.request.standart.StandartRequest;
import common.transfer.response.Response;


/**
 * Команда 'print_field_descending_distance'. Выводит значения поля distance всех элементов в порядке убывания.
 * @author Septyq
 */
public class PrintFieldDescendingDistance extends Command<StandartRequest> {
    private CollectionManager<Entity> collectionManager;

    public PrintFieldDescendingDistance(CollectionManager<Entity> collectionManager) {
        super(new CommandAttribute(
            "print_field_descending_distance",
            "вывести значения поля distance всех элементов в порядке убывания",
            StandartRequest.class
            ));
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(StandartRequest request) {
        List<Integer> distances = collectionManager.getCollection().stream()
            .map(e -> ((Route) e).getDistance())
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());
        return new Response<>(List.of(distances));
    }
}
