package commands;

import java.util.List;

import util.Response;
import util.Status;
import managers.CollectionManager;
import models.Route;


public class Add extends Command {
    private final CollectionManager collectionManager;

    public Add(CollectionManager collectionManager) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
    }
    
    public Response<?> execute(List<?> args) {
        if (args.isEmpty()) {
            return new Response<>(Status.INPUT);
        } else if (args.size() == 1) {
            collectionManager.addToCollection((Route) args.get(0));
            return new Response<>();
        } else {
            return new Response<>(List.of("Invalid argument length"), Status.ERROR);
        }
    }
}
