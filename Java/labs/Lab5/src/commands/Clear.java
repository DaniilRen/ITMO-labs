package commands;

import managers.CollectionManager;

import java.util.List;

import util.Response;
import util.Status;

public class Clear extends Command {
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(List<?> args) {
        if (args.size() > 0) {
            return new Response<>(List.of("Invalid argument length"), Status.ERROR);
        }
        collectionManager.clearCollection();
        return new Response<>();
    }
}
