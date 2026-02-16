package commands;

import java.util.List;

import managers.CollectionManager;
import models.Entity;
import models.Route;
import util.Response;
import util.Status;

public class Update extends Command {
    private final CollectionManager collectionManager;

    public Update(CollectionManager collectionManager) {
        super("update <ID> {element}", "обновить значение элемента коллекции по ID");
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(List<?> args) {
        try {
            if (args.size() == 1){
                return new Response<>(Status.INPUT);
            } else if (args.size() == 2) {
                int id = Integer.parseInt((String) args.get(0));
                Entity oldRoute = collectionManager.getById(id);
                if (oldRoute == null) {
                    return new Response<>(List.of("Item not found"), Status.ERROR);
                }

                oldRoute.update((Route) args.get(1));
                return new Response<>();
             } else {
                return new Response<>(List.of("Invalid argument length"), Status.ERROR);
            }
        } catch (NumberFormatException e) {
            return new Response<>(List.of("Invalid id"), Status.ERROR);
        }
    }
}
