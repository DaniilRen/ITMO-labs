package commands;

import java.util.List;

import managers.CollectionManager;
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
                if (!checkId(args)) {
                    return new Response<>(List.of("Item not found"), Status.ERROR);
                }
                return new Response<>(Status.INPUT);
            } else if (args.size() == 2) {
                if (!checkId(args)) {
                    return new Response<>(List.of("Item not found"), Status.ERROR);
                }   

                int id = Integer.parseInt((String) args.get(0));
                Route newRoute = (Route) args.get(1);
                
                if (collectionManager.updateById(id, newRoute)) {
                    return new Response<>();
                } else {
                    return new Response<>(List.of("Item not found"), Status.ERROR);
                }
             } else {
                return new Response<>(List.of("Invalid argument length"), Status.ERROR);
            }
        } catch (NumberFormatException e) {
            return new Response<>(List.of("Invalid id"), Status.ERROR);
        }
    }

    public boolean checkId(List<?> args) {
        int id = Integer.parseInt((String) args.get(0));
        return (collectionManager.getById(id) != null);
    }
}
