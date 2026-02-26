package commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import managers.CollectionManager;
import util.Response;
import util.Status;

import models.Route;

public class Sort extends Command {
    private final CollectionManager collectionManager;

    public Sort(CollectionManager collectionManager) {
        super("sort", "отсортировать коллекцию в естественном порядке");
        this.collectionManager = collectionManager;
    }

    public Response<?> execute(List<?> args) {
        if (args.size() != 0) {
            return new Response<>(List.of("Invalid argument length"), Status.ERROR);
        } else {
            ArrayList<Route> collection = collectionManager.getCollection();
            Collections.sort(collection);
            return new Response<>(collection);
        }
    }
}
