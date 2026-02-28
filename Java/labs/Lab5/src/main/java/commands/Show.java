package commands;

import java.util.List;

import managers.CollectionManager;
import util.Response;
import util.Status;


/**
 * Команда 'show'. Выводит все элементы коллекции.
 * @author Septyq
 */
public class Show extends Command {
    private final CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        super("show", "вывести все элементы коллекции");
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(List<?> args) {
        if (args.size() > 0) { return new Response<>(List.of("Invalid argument number"), Status.ERROR); }

        return new Response<>(collectionManager.getCollection());
    }
}
